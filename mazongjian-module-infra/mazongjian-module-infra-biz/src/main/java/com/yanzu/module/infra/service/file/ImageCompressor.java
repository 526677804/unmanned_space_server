package com.yanzu.module.infra.service.file;


import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageCompressor {

    private static final long MAX_FILE_SIZE = 256 * 1024; // 256 KB

    /**
     * 压缩图片到目标大小
     *
     * @param inputContent 输入图片的字节数组
     * @return 压缩后的图片字节数组
     * @throws IOException 图片处理异常
     */
    public static byte[] compressImage(byte[] inputContent) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent);
        if (inputContent.length <= MAX_FILE_SIZE) {
            return inputContent;
        }
        BufferedImage originalImage = ImageIO.read(inputStream);
        if (originalImage == null) {
            throw new IllegalArgumentException("无法解析输入图片内容！");
        }

        // 获取图片格式
        String formatName = "jpeg"; // 默认使用 JPEG 格式进行压缩
        String mimeType = getImageMimeType(inputContent); // 获取图片格式

        if ("image/gif".equals(mimeType)) {
            formatName = "gif"; // 如果是 GIF 格式，保持为 GIF 格式
        } else {
            formatName = "jpeg"; // 其它格式统一压缩为 JPEG 格式
        }

        float quality = 1.0f; // 初始质量

        // 检查文件大小并调整质量进行压缩
        while (true) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {
                ImageWriter writer = ImageIO.getImageWritersByFormatName(formatName).next();
                writer.setOutput(imageOutputStream);

                if ("jpeg".equals(formatName)) {
                    // 仅针对 JPEG 格式设置压缩参数
                    JPEGImageWriteParam writeParam = new JPEGImageWriteParam(null);
                    writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    writeParam.setCompressionQuality(quality);
                    writeParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED); // 禁用渐进式模式
                    writer.write(null, new javax.imageio.IIOImage(originalImage, null, null), writeParam);
                } else if ("gif".equals(formatName)) {
                    // 对于 GIF 图片，不进行压缩，直接保存
                    writer.write(null, new javax.imageio.IIOImage(originalImage, null, null), null);
                }
                writer.dispose();
            }

            byte[] compressedData = outputStream.toByteArray();

            if (compressedData.length <= MAX_FILE_SIZE) {
                return compressedData;
            }

            // 如果文件仍然过大，降低质量（仅对 JPEG 格式有效）
            if ("jpeg".equals(formatName)) {
                quality -= 0.1f;
                if (quality < 0.3f) {
                    return compressedData;
                }
            }
        }
    }

    // 获取图片格式的函数
    private static String getImageMimeType(byte[] imageData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        String mimeType = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            if (bufferedImage != null) {
                mimeType = ImageIO.getImageReadersByFormatName("gif").hasNext() ? "image/gif" : "image/jpeg";
                if (ImageIO.getImageReadersByFormatName("png").hasNext()) {
                    mimeType = "image/png";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mimeType;
    }


}
