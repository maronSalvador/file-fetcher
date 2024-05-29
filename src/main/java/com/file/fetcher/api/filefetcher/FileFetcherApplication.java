package com.file.fetcher.api.filefetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class FileFetcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileFetcherApplication.class, args);
	}

}

@RestController
class FileController {
	@Autowired
	private ResourceLoader resourceLoader;
	@GetMapping("/file/{file}")
	public ResponseEntity<byte[]> getFile(@PathVariable String file) throws IOException {
		// Construct the file path
		String filePath = "C:\\Users\\98labs\\Desktop\\File\\" + file; // Change this path to the directory where your files are stored

		// Load the file
		Path path = Paths.get(filePath);

		// Check if the file exists
		if (!Files.exists(path)) {
			return ResponseEntity.notFound().build();
		}

		try {
			// Read the file content
			byte[] content = Files.readAllBytes(path);

			// Set up HttpHeaders for the response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", file);

			// Return the ResponseEntity with the file content and headers
			return ResponseEntity.ok()
					.headers(headers)
					.body(content);
		} catch (IOException e) {
			// Handle IO exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
