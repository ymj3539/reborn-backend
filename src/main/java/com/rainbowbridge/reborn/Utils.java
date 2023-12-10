package com.rainbowbridge.reborn;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Utils {

    private Utils() {
        throw new AssertionError("Cannot create instance of this class");
    }

    // 이미지 경로 산출
    public static String getImagePath(String imageName) {
        return "http://146.56.104.45:8080/home/opc/reborn-backend/src/main/resources/images/"+imageName+".png";
    }

    // 날짜 형식 변환
    public static String convertLocalDateFormat(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd(EEE)", Locale.KOREAN);
        return date.format(formatter);
    }

    public static String convertLocalDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    // 시간 형식 변환
    public static String convertTimeRangeFormat(int openTime, int closeTime) {
        String openTimeString = convertTimeFormat(openTime);
        String closeTimeString = convertTimeFormat(closeTime);
        return openTimeString + " - " + closeTimeString;
    }

    public static String convertTimeFormat(int time) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(time) + ":" + decimalFormat.format(0);
    }

    public static String convertHourTo12HourFormat (int hour) {
        String period = (hour < 12) ? "오전" : "오후";

        if (hour > 12) {
            hour -= 12;
        }

        String formattedHour = String.format("%02d", hour);

        return period + " " + formattedHour + ":00";
    }

    // 거리 계산 - Vincenty 공식 사용
    public static double calculateDistance(double userLatitude, double userLongitude, double companyLatitude, double companyLongitude) {
        double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563;  // WGS-84 ellipsoid params
        double L = Math.toRadians(companyLongitude - userLongitude);
        double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(userLatitude)));
        double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(companyLatitude)));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
        double lambda = L, lambdaP, iterLimit = 100;
        do {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0) return 0;  // co-incident points
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            if (Double.isNaN(cos2SigmaM)) cos2SigmaM = 0;  // equatorial line: cosSqAlpha=0 (§6)
            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0) return 0;  // formula failed to converge

        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        double s = b * A * (sigma - deltaSigma);

        return s;
    }

}
