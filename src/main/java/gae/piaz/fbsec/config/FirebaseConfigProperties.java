package gae.piaz.fbsec.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "firebase")
@Data
public class FirebaseConfigProperties {
    private String type;
    private String project_id;
    private String private_key;
    private String private_key_id;
    private String client_email;
    private String client_id;
    private String token_uri;
    private String url;
}
