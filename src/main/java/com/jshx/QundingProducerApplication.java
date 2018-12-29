package com.jshx;

import com.codahale.metrics.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@ComponentScan(basePackages = {"com.jshx"})
public class QundingProducerApplication implements CommandLineRunner {

	@Override
	public void run(String... arg0)  {
		if (arg0.length > 0 && arg0[0].equals("exitcode")) {
			throw new ExitException();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(QundingProducerApplication.class, args);
	}

	class ExitException extends RuntimeException implements ExitCodeGenerator {
		private static final long serialVersionUID = 1L;

		@Override
		public int getExitCode() {
			return 10;
		}
	}


	@Bean
	public MetricRegistry metricRegistry() {
		return new MetricRegistry();
	}

	/**
	 * TPS 计算器
	 *
	 * @param metricRegistry
	 * @return
	 */
	@Bean
	public Meter requestMeter(MetricRegistry metricRegistry) {
		return metricRegistry.meter("request");
	}

	/**
	 * 直方图
	 *
	 * @param metricRegistry
	 * @return
	 */
	@Bean
	public Histogram responseSizes(MetricRegistry metricRegistry) {
		return metricRegistry.histogram("response-sizes");
	}

	/**
	 * 计数器
	 *
	 * @param metricRegistry
	 * @return
	 */
	@Bean
	public Counter pendingJobs(MetricRegistry metricRegistry) {
		return metricRegistry.counter("requestCount");
	}

	/**
	 * 计时器
	 *
	 * @param metricRegistry
	 * @return
	 */
	@Bean
	public Timer responses(MetricRegistry metricRegistry) {
		return metricRegistry.timer("executeTime");
	}
}
