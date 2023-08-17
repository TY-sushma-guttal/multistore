package com.mrmrscart.userservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

public class JsonSchemaValidator {

	public boolean jsonSchemaValidator(String staffDetails) {
		JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
		InputStream schemaStream = null;
		boolean mismatch = false;
		try {
			JsonNode json = null;
			json = stringToJsonTree(staffDetails);
			int nodeCount = json.get("staffCapabilityList").size();
			
			for(int i=0;i<nodeCount;i++) {
				JsonNode singleNode = json.get("staffCapabilityList").get(i);
				System.out.println(singleNode.get("cabilityType"));
				File jsonFilePath = new File(
						getClass().getClassLoader().getResource("staff_capabilities_json_schema.json").toURI());
				schemaStream = new FileInputStream(jsonFilePath);
				JsonSchema parentSchema = schemaFactory.getSchema(schemaStream);
				Set<ValidationMessage> schemaValidationResult = parentSchema.validate(singleNode);
				if(!schemaValidationResult.isEmpty()) {
					mismatch = printErrorMessageForResponse(schemaValidationResult,singleNode);
				}
			}
			if(mismatch) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean printErrorMessageForResponse(Set<ValidationMessage> schemaValidationResult,
			JsonNode staffCapabilityList) {
		Map<String, List<String>> errorTrackMap = new HashMap<>();
		for (ValidationMessage vm : schemaValidationResult) {
			String[] validationMessageSplitIntoDataAndError = vm.getMessage().split(":");
			String dataAddress = validationMessageSplitIntoDataAndError[0];
			String errorMessage = validationMessageSplitIntoDataAndError[1];
			String[] dataAddressArray = dataAddress.split("\\.");
			if (null == staffCapabilityList.get("staffCapabilityList")) {
				String capabilityType = nodeValueToString(staffCapabilityList.get("cabilityType").toString());

				if (errorTrackMap.get(capabilityType) == null) {
					errorTrackMap.put(capabilityType, new ArrayList<>());
				}
				errorTrackMap.get(capabilityType).add(String.join("->", dataAddressArray) + ":" + errorMessage);
			}else {
				if (errorTrackMap.get("Parent") == null) {
					errorTrackMap.put("Parent", new ArrayList<>());
				}
				errorTrackMap.get("Parent").add(String.join("->", dataAddressArray) + ":" + errorMessage);
			}
		}
		if(!errorTrackMap.isEmpty()) {
			return true;
		}
		return false;
	}

	private JsonNode stringToJsonTree(String jsonString) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(jsonString);
		JsonNode json = null;
		try {
			json = mapper.readTree(parser);
		} catch (Exception e) {
			throw new Exception("incomplete response received from api");
		}
		return json;
	}

	private String nodeValueToString(String input) {
		if (input != null)
			return input.replace("\"", "");
		return input;
	}
}

