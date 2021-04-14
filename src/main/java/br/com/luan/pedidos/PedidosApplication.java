package br.com.luan.pedidos;

import br.com.luan.pedidos.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PedidosApplication extends SpringBootServletInitializer implements CommandLineRunner {

	//somente para teste do AmazonS3
	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(PedidosApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PedidosApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadFile("C:\\FOTOS - NAO TEM BACKUP AINDA\\moto g5s\\Camera\\IMG_20180606_211007913.jpg");
	}
}
