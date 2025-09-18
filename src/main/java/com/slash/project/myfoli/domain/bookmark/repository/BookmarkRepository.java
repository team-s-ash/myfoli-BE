package com.slash.project.myfoli.domain.bookmark.repository;

import com.slash.project.myfoli.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
