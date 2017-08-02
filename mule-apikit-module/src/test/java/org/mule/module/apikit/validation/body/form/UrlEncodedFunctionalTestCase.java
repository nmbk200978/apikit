package org.mule.module.apikit.validation.body.form;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.test.runner.ArtifactClassLoaderRunnerConfig;

import com.jayway.restassured.RestAssured;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

@ArtifactClassLoaderRunnerConfig
public class UrlEncodedFunctionalTestCase extends MuleArtifactFunctionalTestCase
{
    @Rule
    public DynamicPort serverPort = new DynamicPort("serverPort");

    @Override
    public int getTestTimeoutSecs()
    {
        return 6000;
    }

    @Override
    protected void doSetUp() throws Exception
    {
        RestAssured.port = serverPort.getNumber();
        super.doSetUp();
    }

    @Override
    protected String getConfigResources()
    {
        return "org/mule/module/apikit/validation/formParameters/mule-config.xml";
    }

    @Test
    public void setDefaultFormParameterForUrlencodedRequest() throws Exception
    {
        given().header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("second", "segundo")
                .formParam("third", "true")
                .expect()
                .response()
                .body(is("primo"))
                .statusCode(201)
                .when().post("/api/url-encoded");
    }
}
