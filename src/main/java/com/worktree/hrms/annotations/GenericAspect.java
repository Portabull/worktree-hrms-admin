package com.worktree.hrms.annotations;

import com.worktree.hrms.dao.CommonDao;
import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.scanners.FileScanner;
import com.worktree.hrms.utils.RequestHelper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Aspect
@Component
@RequiredArgsConstructor
public class GenericAspect {

    private final CommonDao commonDao;

    private final FileScanner fileScanner;

    @Around("@annotation(Feature)")
    public Object validateFeature(ProceedingJoinPoint joinPoint) throws Throwable {
        Feature feature = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Feature.class);
        commonDao.userHasFeature(feature.feature());
        return joinPoint.proceed();
    }

    @Around("@annotation(FileScan)")
    public Object fileScan(ProceedingJoinPoint joinPoint) throws Throwable {

        FileScan fileScan = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(FileScan.class);

        long maxFileSize = fileScan.size();

        RequestHelper.getCurrentRequest().getParts().forEach(part -> {
            if (part.getContentType() != null) {

                MultipartFile file = new MultipartFile() {
                    @Override
                    public String getName() {
                        return part.getName();
                    }

                    @Override
                    public String getOriginalFilename() {
                        return part.getSubmittedFileName();
                    }

                    @Override
                    public String getContentType() {
                        return part.getContentType();
                    }

                    @Override
                    public boolean isEmpty() {
                        return part.getSize() == 0;
                    }

                    @Override
                    public long getSize() {
                        return part.getSize();
                    }

                    @Override
                    public byte[] getBytes() throws IOException {
                        return part.getInputStream().readAllBytes();
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return part.getInputStream();
                    }

                    @Override
                    public void transferTo(File dest) throws IOException, IllegalStateException {
                        Files.copy(part.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                };

                if (maxFileSize > 0 && file.getSize() > maxFileSize) {
                    throw new BadRequestException(fileScan.message());
                }

                fileScanner.scanFile(file);
            }
        });

        return joinPoint.proceed();
    }

}
