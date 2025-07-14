package org.springframework.samples.petclinic.system;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.PetClinicMcpTools;

@Configuration
class McpConfig {

	@Bean
	ToolCallbackProvider mcpTools(PetClinicMcpTools petClinicMcpTools) {
		return MethodToolCallbackProvider.builder().toolObjects(petClinicMcpTools).build();
	}

}
