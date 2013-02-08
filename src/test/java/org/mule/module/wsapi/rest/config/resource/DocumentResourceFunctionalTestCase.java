
package org.mule.module.wsapi.rest.config.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

import com.jayway.restassured.RestAssured;

import org.junit.Rule;
import org.junit.Test;

public class DocumentResourceFunctionalTestCase extends FunctionalTestCase
{
    @Rule
    public DynamicPort serverPort = new DynamicPort("serverPort");

    @Override
    protected void doSetUp() throws Exception
    {
        RestAssured.port = serverPort.getNumber();
        super.doSetUp();
    }

    @Override
    protected String getConfigResources()
    {
        return "org/mule/module/wsapi/rest/resource/document-resource-config.xml, org/mule/module/wsapi/test-flows-config.xml";
    }

    @Test
    public void documentNotFound() throws Exception
    {
        expect().response().log().everything().statusCode(404).when().head("/api/league1");
    }

    @Test
    public void noCreateOnDocument() throws Exception
    {
        expect().response().log().everything().statusCode(405).when().head("/api/league");
    }

    @Test
    public void retrieveOnDocument() throws Exception
    {
        expect().log().everything().response().statusCode(200)
            .body(containsString("Liga BBVA")).when().get("/api/league");
    }

    @Test
    public void retrieveOnNestedDocument() throws Exception
    {
        expect().log().everything().response().statusCode(200)
            .body(containsString("Royal")).when().get("/api/league/association");
    }

    @Test
    public void updateOnDocument() throws Exception
    {
        given().body("Premier League").expect().response().statusCode(200).when().put("/api/league");
    }

    @Test
    public void noDeleteOnDocument() throws Exception
    {
        expect().log().everything().response().statusCode(405).when().head("/api/league");
    }

}
