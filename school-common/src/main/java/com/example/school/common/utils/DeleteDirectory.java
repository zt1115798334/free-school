package com.example.school.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/18 10:32
 * description:
 */
@Slf4j
public class DeleteDirectory implements FileVisitor<Path> {
    private boolean deleteFileByFile(Path file) throws IOException {
        return Files.deleteIfExists(file);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        boolean success = deleteFileByFile(file);

        if (success) {
            log.info("Deleted: {}", file);
        } else {
            log.info("Not deleted: {}", file);
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (exc == null) {
            log.info("Visited: {}", dir);
            boolean success = deleteFileByFile(dir);

            if (success) {
                log.info("Deleted: {}", dir);
                System.out.println("Deleted: " + dir);
            } else {
                log.info("Not deleted: {}", dir);
                System.out.println("Not deleted: " + dir);
            }
        } else {
            throw exc;
        }
        return FileVisitResult.CONTINUE;
    }


}
