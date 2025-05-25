package com.exemplo.garage.controller;

import java.io.*;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.util.*;

public class MultipartBodyPublisher {

    public static class Builder {
        private final String boundary;
        private final List<byte[]> parts = new ArrayList<>();

        public Builder(String boundary) {
            this.boundary = boundary;
        }

        public Builder addFilePart(String name, File file) throws IOException {
            String mimeType = Files.probeContentType(file.toPath());
            String filename = file.getName();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(("--" + boundary + "\r\n").getBytes());
            output.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n").getBytes());
            output.write(("Content-Type: " + mimeType + "\r\n\r\n").getBytes());
            output.write(Files.readAllBytes(file.toPath()));
            output.write("\r\n".getBytes());

            parts.add(output.toByteArray());
            return this;
        }

        public HttpRequest.BodyPublisher build() throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            for (byte[] part : parts) {
                output.write(part);
            }
            output.write(("--" + boundary + "--\r\n").getBytes());
            return HttpRequest.BodyPublishers.ofByteArray(output.toByteArray());
        }
    }

    public static Builder newBuilder(String boundary) {
        return new Builder(boundary);
    }
}
