package com.gasfgrv.graphql.demo_graphql;

import org.springframework.boot.SpringApplication;

public class TestDemoGraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoGraphqlApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
