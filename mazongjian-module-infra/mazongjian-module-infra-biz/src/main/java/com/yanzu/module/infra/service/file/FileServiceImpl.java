package com.yanzu.module.infra.service.file;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.common.util.io.FileUtils;
import com.yanzu.framework.file.core.client.FileClient;
import com.yanzu.framework.file.core.utils.FileTypeUtils;
import com.yanzu.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.yanzu.module.infra.dal.dataobject.file.FileDO;
import com.yanzu.module.infra.dal.mysql.file.FileMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.infra.enums.ErrorCodeConstants.FILE_NOT_EXISTS;

/**
 * 文件 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileMapper fileMapper;

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    public static byte[] compressImage(byte[] content) throws IOException {
        final long MAX_SIZE = 256 * 1024; // 256 KB

        // Step 1: Load the image
        BufferedImage image  = ImageIO.read(new ByteArrayInputStream(content));
        if (image == null) {
            throw new IllegalArgumentException("Invalid image content");
        }
        // Step 2: Convert image to standard RGB format (to handle transparency or unsupported color spaces)
        BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgbImage.createGraphics();
        g.drawImage(image, 0, 0, Color.WHITE, null); // Fill with white if there are transparent areas
        g.dispose();
        image = rgbImage;
        // Step 3: Compress the image
        ByteArrayOutputStream compressedOutput = new ByteArrayOutputStream();
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found for JPEG format");
        }
        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

        float quality = 0.9f; // Initial quality setting
        byte[] result = content;

        do {
            compressedOutput.reset(); // Clear previous output
            param.setCompressionQuality(quality);

            try (MemoryCacheImageOutputStream output = new MemoryCacheImageOutputStream(compressedOutput)) {
                writer.setOutput(output);
                writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
            }

            result = compressedOutput.toByteArray();
            quality -= 0.1f; // Reduce quality step-by-step
        } while (result.length > MAX_SIZE && quality > 0.1f);

        writer.dispose();
        return result;
    }

    @Override
    @SneakyThrows
    public String createFile(String name, String path, byte[] content) {
        content = compressImage(content);
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);
        return url;
    }

    @Override
    public void deleteFile(Long id) throws Exception {
        // 校验存在
        FileDO file = validateFileExists(id);

        // 从文件存储器中删除
        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.delete(file.getPath());

        // 删除记录
        fileMapper.deleteById(id);
    }

    private FileDO validateFileExists(Long id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return fileDO;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }

}
