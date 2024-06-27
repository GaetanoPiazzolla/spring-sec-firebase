package gae.piaz.fbsec.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Autowired private FirebaseConfigProperties firebaseConfigProperties;

    @PostConstruct
    public void initialize() throws IOException {
        try {

            firebaseConfigProperties.setPrivate_key(
                    firebaseConfigProperties.getPrivate_key().replace("\\n", "\n"));

            String json = new Gson().toJson(firebaseConfigProperties);

            GoogleCredentials credentials =
                    GoogleCredentials.fromStream(new ByteArrayInputStream(json.getBytes()));

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(credentials)
                            .setDatabaseUrl(firebaseConfigProperties.getUrl())
                            .build();

            FirebaseApp.initializeApp(options);

            log.info("Firebase initialized for URL {}", firebaseConfigProperties.getUrl());
        } catch (IOException e) {
            log.error("Firebase config error", e);
            throw e;
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

}
