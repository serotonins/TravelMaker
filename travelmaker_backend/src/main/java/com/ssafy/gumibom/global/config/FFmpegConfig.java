//package com.ssafy.gumibom.global.config;
//
//import net.bramp.ffmpeg.FFmpeg;
//import net.bramp.ffmpeg.FFprobe;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;
//import org.bytedeco.javacpp.Loader;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.aot.AotServices;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//public class FFmpegConfig {
//
////    @Value("${ffmpeg.main.location}")
////    public String ffmpegLocation;
////
////    @Value("${ffmpeg.probe.location}")
////    public String ffprobeLocation;
//
//    public String ffmpegLocation = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
//    public String ffprobeLocation = Loader.load(org.bytedeco.ffmpeg.ffprobe.class);
//
//    @Bean
//    public FFmpeg ffMpeg() throws IOException {
////        ClassPathResource classPathResource = new ClassPathResource(ffmpegLocation);
////        return new FFmpeg(classPathResource.getURL().getPath());
//
////        InputStream inputStream = new ClassPathResource(ffmpegLocation).getInputStream();
////        File file = File.createTempFile("ffmpeg", ".exe");
////
////        try {
////            FileUtils.copyInputStreamToFile(inputStream, file);
////        } finally {
////            IOUtils.closeQuietly(inputStream);
////        }
////
////        return new FFmpeg(file.getPath());
//
//        FFmpeg ffMPeg = null;
//
//        String osName = System.getProperty("os.name");
//
//        // 운영체제가 Window인 경우 jar에 내장되어있는 ffmpeg 를 이용
//        if (osName.toLowerCase().contains("win")) {
//            ClassPathResource classPathResource = new ClassPathResource(ffmpegLocation);
//            ffMPeg = new FFmpeg(classPathResource.getURL().getPath());
//        } else if(osName.toLowerCase().contains("unix") || osName.toLowerCase().contains("linux")) {
//            ffMPeg = new FFmpeg(ffmpegLocation);
//        }
//
//        return ffMPeg;
//    }
//
//    @Bean
//    public FFprobe ffProbe() throws IOException {
////        ClassPathResource classPathResource = new ClassPathResource(ffprobeLocation);
////        return new FFprobe(classPathResource.getURL().getPath());
//
////        InputStream inputStream = new ClassPathResource(ffprobeLocation).getInputStream();
////        File file = File.createTempFile("ffprobe", ".exe");
////
////        try {
////            FileUtils.copyInputStreamToFile(inputStream, file);
////        } finally {
////            IOUtils.closeQuietly(inputStream);
////        }
////
////        return new FFprobe(file.getPath());
//
//        FFprobe ffprobe = null;
//
//        String osName = System.getProperty("os.name");
//
//        // 운영체제가 Window인 경우 jar에 내장되어있는 ffmpeg 를 이용
//        if (osName.toLowerCase().contains("win")) {
//            ClassPathResource classPathResource = new ClassPathResource(ffprobeLocation);
//            ffprobe = new FFprobe(classPathResource.getURL().getPath());
//        } else if(osName.toLowerCase().contains("unix") || osName.toLowerCase().contains("linux")) {
//            ffprobe = new FFprobe(ffprobeLocation);
//        }
//
//        return ffprobe;
//    }
//}
