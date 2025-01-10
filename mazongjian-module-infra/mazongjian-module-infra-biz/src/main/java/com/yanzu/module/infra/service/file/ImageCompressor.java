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

        // 强制转换为 RGB 格式，确保兼容性
        BufferedImage rgbImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgbImage.getGraphics().drawImage(originalImage, 0, 0, null);

        String formatName = "jpeg"; // 使用 JPEG 格式进行压缩
        float quality = 1.0f; // 初始质量

        // 检查文件大小并调整质量进行压缩
        while (true) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {
                ImageWriter writer = ImageIO.getImageWritersByFormatName(formatName).next();
                writer.setOutput(imageOutputStream);

                // 设置压缩参数
                JPEGImageWriteParam writeParam = new JPEGImageWriteParam(null);
                writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                writeParam.setCompressionQuality(quality);
                writeParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED); // 禁用渐进式模式

                // 写入压缩后的图片
                writer.write(null, new javax.imageio.IIOImage(rgbImage, null, null), writeParam);
                writer.dispose();
            }

            byte[] compressedData = outputStream.toByteArray();

            if (compressedData.length <= MAX_FILE_SIZE) {
                return compressedData;
            }

            // 如果文件仍然过大，降低质量
            quality -= 0.1f;
            if (quality < 0.3f) {
                return compressedData;
            }
        }
    }
}
