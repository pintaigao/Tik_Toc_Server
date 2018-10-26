package com.imooc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {

    private String ffmpegEXE;

    public MergeVideoMp3(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath, String mp3InputPath, double seconds, String videoOutputPath) throws IOException {

        System.out.println(ffmpegEXE);
        System.out.println(mp3InputPath);
        List<String> command = new ArrayList<>();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-i");
        command.add(mp3InputPath);
        command.add("-t");
        command.add(String.valueOf(seconds));

        command.add("-y");
        command.add(videoOutputPath);

        /* To Stop the Process and release the memory space  */
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String line = "";
        while ((line = br.readLine()) != null) {

        }

        if (br != null) {
            br.close();
        }

        if (inputStreamReader != null) {
            inputStreamReader.close();
        }

        if (errorStream != null) {
            errorStream.close();
        }


    }
}
