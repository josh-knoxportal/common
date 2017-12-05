package com.nemustech.common.mapper;

import org.springframework.stereotype.Repository;

import com.nemustech.common.file.Files;

/**
 * 파일 매퍼
 * 
 * @author skoh
 */
@Repository
public interface FilesMapper<T extends Files> extends CommonMapper<T> {
}