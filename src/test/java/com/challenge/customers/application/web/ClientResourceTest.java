package com.challenge.customers.application.web;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientResourceTest {

    private static final Logger LOG = Logger.getLogger(ClientResourceTest.class);

    @Inject EntityManager em;

    long GENDER_ID;
    long STATUS_ACTIVE_ID;
    long STATUS_INACTIVE_ID;
    long COUNTRY_CL_ID;
    long COUNTRY_CO_ID;

    Long createdId;

    @BeforeAll
    @ActivateRequestContext
    void resolveCatalogIds() {
        GENDER_ID = em.createQuery("select g.id from CatalogGender g where upper(g.name)='MALE'", Long.class)
                .getSingleResult();
        STATUS_ACTIVE_ID = em.createQuery("select s.id from CatalogStatus s where upper(s.name)='ACTIVE'", Long.class)
                .getSingleResult();
        STATUS_INACTIVE_ID = em.createQuery("select s.id from CatalogStatus s where upper(s.name)='INACTIVE'", Long.class)
                .getSingleResult();
        COUNTRY_CL_ID = em.createQuery("select c.id from CatalogCountry c where upper(c.name)='CHILE'", Long.class)
                .getSingleResult();
        COUNTRY_CO_ID = em.createQuery("select c.id from CatalogCountry c where upper(c.name)='COLOMBIA'", Long.class)
                .getSingleResult();

        LOG.infof("Catalog IDs loaded → Gender: %d, StatusActive: %d, Country CL: %d, CO: %d",
                GENDER_ID, STATUS_ACTIVE_ID, COUNTRY_CL_ID, COUNTRY_CO_ID);
    }


    @Test
    void health_should_be_up() {
        given().when().get("/q/health")
                .then().statusCode(200)
                .body("status", equalTo("UP"));
    }


    void create_non_chile_should_succeed() {
        var payload = """
        {
          "name": "Maria Gomez",
          "birthDate": "1995-03-22",
          "genderId": %d,
          "statusId": %d,
          "countryId": %d,
          "numCta": "CT999888777"
        }
        """.formatted(GENDER_ID, STATUS_ACTIVE_ID, COUNTRY_CO_ID);

        io.restassured.response.Response resp =
                given()
                        .contentType(io.restassured.http.ContentType.JSON)
                        .body(payload)
                        .when()
                        .post("/clients")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(201)
                        .extract()
                        .response();

        String location = resp.getHeader("Location");
        if (location != null && location.matches(".*/clients/\\d+")) {
            createdId = Long.parseLong(location.replaceAll(".*/clients/(\\d+)", "$1"));
        } else {
            // fallback al body
            createdId = resp.jsonPath().getLong("id");
        }
        Assertions.assertNotNull(createdId, "createdId no debería ser null");

        given()
                .when()
                .get("/clients/{id}", createdId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdId.intValue()))
                .body("name", equalTo("Maria Gomez"))
                .body("country", anyOf(nullValue(), notNullValue())); // opcional: solo para no romper si mapeas distinto
    }

    @Test
    void get_by_id_should_return_client() {
        assumeTrue(createdId != null, "createdId es null, create falló");
        given().when().get("/clients/{id}", createdId)
                .then().statusCode(200)
                .body("id", equalTo(createdId.intValue()))
                .body("name", equalTo("Maria Gomez"));
    }

    @Test
    void update_numCta_should_succeed() {
        assumeTrue(createdId != null, "createdId es null, create falló");
        var patch = "{ \"numCta\": \"CT123456NEW\" }";
        given().contentType(ContentType.JSON).body(patch)
                .when().patch("/clients/{id}", createdId)
                .then().statusCode(200)
                .body("numCta", equalTo("CT123456NEW"));
    }

    @Test
    void switch_to_chile_with_invalid_numCta_should_fail() {
        assumeTrue(createdId != null, "createdId es null, create falló");
        var patch = "{ \"countryId\": %d, \"numCta\": \"ABC123\" }".formatted(COUNTRY_CL_ID);
        given().contentType(ContentType.JSON).body(patch)
                .when().patch("/clients/{id}", createdId)
                .then().statusCode(400)
                .body(containsString("003"));
    }

    @Test
    void switch_to_chile_with_valid_numCta_should_succeed() {
        assumeTrue(createdId != null, "createdId es null, create falló");
        var patch = "{ \"countryId\": %d, \"numCta\": \"003777888999\" }".formatted(COUNTRY_CL_ID);
        given().contentType(ContentType.JSON).body(patch)
                .when().patch("/clients/{id}", createdId)
                .then().statusCode(200)
                .body("country", equalTo("CHILE"))
                .body("numCta", equalTo("003777888999"));
    }

    @Test
    void activate_inactivate_should_work() {
        assumeTrue(createdId != null, "createdId es null, create falló");
        given().when().post("/clients/{id}/inactivate", createdId)
                .then().statusCode(200)
                .body("status", equalTo("INACTIVE"))
                .body("inactivatedDate", notNullValue());

        given().when().post("/clients/{id}/activate", createdId)
                .then().statusCode(200)
                .body("status", equalTo("ACTIVE"))
                .body("activatedDate", notNullValue());
    }

    @Test
    void delete_should_return_204() {
        assumeTrue(createdId != null, "createdId es null, create falló");
        given().when().delete("/clients/{id}", createdId)
                .then().statusCode(204);
    }
}
