package com.rainbowbridge.reborn.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/*
* firebase의 비공개 키 파일의 인증정보를 이용한 FirebaseApp 초기화
 */
@Configuration
public class FCMConfig {
    @Value("${firebase.secret.key}")
    private String googleApplicationCredentials;

    FirebaseMessaging firebaseMessaging() throws IOException {
        ClassPathResource resource = new ClassPathResource(googleApplicationCredentials);
        InputStream refreshToken = resource.getInputStream();

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

        if(firebaseAppList != null && !firebaseAppList.isEmpty()){
            for(FirebaseApp app : firebaseAppList) {
                if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
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
