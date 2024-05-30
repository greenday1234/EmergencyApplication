package project.emergencyApplication.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FCMConfig {

    private String fcmPath;

    public FCMConfig(@Value("${fcm.path}") String fcmPath) {
        this.fcmPath = fcmPath;
    }

    /**
     * SDK 추가 로직
     */
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {

        // ClassPathResource 를 사용해 resource 폴더에 접근
        ClassPathResource resource = new ClassPathResource(fcmPath);

        InputStream refreshToken = resource.getInputStream();

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

        // 일치하는 앱 이름을 조회한 뒤 초기화된 앱이 있으면 해당 앱을 사용하고, 아니면 앱을 초기화 한다.
        if (firebaseAppList != null && !firebaseAppList.isEmpty()) {
            for (FirebaseApp app : firebaseAppList) {
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    firebaseApp = app;
                }
            }
        } else {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
        }
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
