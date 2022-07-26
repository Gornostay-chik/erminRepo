package com.example.myRest;

import JsonObjects.myObjectJSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class myController {

    private static final Logger LOGGER = LoggerFactory.getLogger(myController.class);

    /*Input JSON (Sample for Postman):
    {
   "correlationId": "12",
   "techData": [
      {
         "correlationId": "111",
         "actionId": "0",
         "responseCode": "0",
         "errorDescription": null,
         "isDataFromCache": true
      },
      {
         "correlationId": "222",
         "actionId": "1",
         "responseCode": "0",
         "errorDescription": null,
         "isDataFromCache": true
      }],
   "businessData": {
      "result": "1"
   }
}
    */
    @GetMapping(value="/complicated_json")
    public void getComplicatedJson(@RequestBody String strJsonWithArray){
        LOGGER.info("METHOD CALL: \"/complicated_json\"");
        LOGGER.info("INPUT JSON: " + strJsonWithArray);

        //задержка
        int seconds = 20;
        int randomSec = ThreadLocalRandom.current().nextInt(1, 5);
        try {
            Thread.sleep(randomSec * 1000);
        } catch (InterruptedException e) {
            LOGGER.error("Random delay error: "+e.toString());
        }

        try {
            ObjectMapper inputObjectMapper = new ObjectMapper();
            JsonNode inputJsonNode = inputObjectMapper.readTree(strJsonWithArray);

            Iterator<Map.Entry<String, JsonNode>> fields = inputJsonNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> jsonField = fields.next();
                if (jsonField.getValue().isValueNode()) {
                    System.out.println("This is ValueNode:   " +jsonField.toString());
                }
                else if (jsonField.getValue().isArray()) {
                    System.out.println("This is Array:   " + jsonField.toString());
                }
                else if (jsonField.getValue().isObject()) {
                    System.out.println("This is Object:   " + jsonField.toString());;
                }
            }

        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

    }

    /*Input JSON (Sample for Postman):
    {
  "correlationId": "12",
  "techData": {
    "correlationId": "111",
    "actionId": "0",
    "responseCode": "0",
    "errorDescription": null,
    "isDataFromCache": true
  },
  "businessData": {
    "result": "1"
  }
}
     */
    @PostMapping(value="/json_str", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getString(@RequestBody String myStr) {
        LOGGER.info("METHOD CALL: \"/json_str\"");
        LOGGER.info("INPUT JSON: " + myStr);

        //задержка
        int seconds = 20;
        int randomSec = ThreadLocalRandom.current().nextInt(1, 5);
        try {
            Thread.sleep(randomSec * 1000);
        } catch (InterruptedException e) {
            LOGGER.error("Random delay error: "+e.toString());
        }

        Resource myResourse = new ClassPathResource("/templates/json_str.json");

        try {
            //основной JSON из templates
            ObjectMapper myObjectMapper = new ObjectMapper();
        /*создала новые значения для подмены в json
        Это нужно, потому что в методе set() должен быть объект типа JsonNode.
        То есть мы вставляем именно кусочек структуры нового JSON!*/
            ObjectMapper omCorrID = new ObjectMapper();
            ObjectMapper omActID = new ObjectMapper();
            JsonNode jnCorrID = omCorrID.readTree(myStr).get("techData").get("correlationId");
            JsonNode jnActID = omActID.readTree(myStr).get("techData").get("actionId");

            //считала основной JSON из template
            JsonNode myJsonNode = myObjectMapper.readTree(myResourse.getInputStream());
            //заменила значения на новые
            ((ObjectNode) myJsonNode.get("message")).set("CorrelationId", jnCorrID);
            ((ObjectNode) myJsonNode.get("message")).set("ActionId", jnActID);

            LOGGER.info("OUTPUT JSON: " + myJsonNode);
            return myJsonNode;
            //return new ResponseEntity(mapper.readValue(resource.getInputStream(), Object.class), HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }

    /*Input JSON (Sample for Postman):
    {
"correlationId":"12",
"techData":[
{
"correlationId":"111",
"actionId":"0",
"responseCode":"0",
"errorDescription":null,
"isDataFromCache":true
},
{
"correlationId":"222",
"actionId":"1",
"responseCode":"0",
"errorDescription":null,
"isDataFromCache":true
}],
"businessData":{
"result":"1",
"tag":"2",
"name":"Name of business operation"
}
}
     */
    @GetMapping(value="/json_array")
    public Object getJsonArray(@RequestBody String strJsonWithArray){
        LOGGER.info("METHOD CALL: \"/json_array\"");
        LOGGER.info("INPUT JSON: " + strJsonWithArray);

        //задержка
        int seconds = 20;
        int randomSec = ThreadLocalRandom.current().nextInt(1, 5);
        try {
            Thread.sleep(randomSec * 1000);
        } catch (InterruptedException e) {
            LOGGER.error("Random delay error: "+e.toString());
        }

        try{
            ObjectMapper inputObjectMapper = new ObjectMapper();
            JsonNode inputJsonArray = inputObjectMapper.readTree(strJsonWithArray).get("techData");
            if (inputJsonArray.isArray()) {
                System.out.println("Yes, it's array!");
                //obtain in cycle values of the array for keys "correlationId" and "actionID"
                for (JsonNode arrayItem : inputJsonArray){
                    System.out.println(arrayItem.get("correlationId").toString());
                    System.out.println(arrayItem.get("actionId").toString());
                }
                //obtain the 2-nd element of the arrays
                ArrayNode arrayNode = (ArrayNode) inputJsonArray;
                System.out.println("The second arrays element is " + arrayNode.get(1).toString() + ". And it's " + arrayNode.get(1).getClass().toString());
            }
            else {
                System.out.println("No, it isn't array!");
            }


        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return null;

    }

    /*INPUT JSON (Sample for Postman):
    {
        "correlationId": "12",
        "actionId": "654321"
    }
    */
    @PostMapping(value="/json_obj", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getObject(@RequestBody myObjectJSON myObject ) {
        LOGGER.info("METHOD CALL: \"/json_obj\"");
        LOGGER.info("INPUT JSON: " + myObject.toString());

        //задержка
        int seconds = 20;
        int randomSec = ThreadLocalRandom.current().nextInt(1, 5);
        try {
            Thread.sleep(randomSec * 1000);
        } catch (InterruptedException e) {
            LOGGER.error("Random delay error: "+e.toString());
        }

        Resource myResourse = new ClassPathResource("/templates/json_obj.json");

        try {
            //основной JSON из templates
            ObjectMapper myObjectMapper = new ObjectMapper();
            //создала новые значения для подмены в json
            ObjectMapper omCorrID = new ObjectMapper();
            ObjectMapper omActID = new ObjectMapper();
            JsonNode jnCorrID = omCorrID.readTree(myObject.getCorrelationId());
            JsonNode jnActID = omActID.readTree(myObject.getActionId());

            //считала основной JSON из template
            JsonNode myJsonNode = myObjectMapper.readTree(myResourse.getInputStream());
            //заменила значения на новые
            ((ObjectNode) myJsonNode.get("techData")).set("correlationId", jnCorrID);
            ((ObjectNode) myJsonNode.get("techData")).set("actionId", jnActID);

            LOGGER.info("OUTPUT JSON: " + myJsonNode);
            return myJsonNode;
            //return new ResponseEntity(mapper.readValue(resource.getInputStream(), Object.class), HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }



}
