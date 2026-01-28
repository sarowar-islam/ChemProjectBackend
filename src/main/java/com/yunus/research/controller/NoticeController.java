package com.yunus.research.controller;

import com.yunus.research.dto.NoticeDto;
import com.yunus.research.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeDto>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<NoticeDto>> getRecentNotices(@RequestParam(defaultValue = "3") int limit) {
        return ResponseEntity.ok(noticeService.getRecentNotices(limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> getNoticeById(@PathVariable String id) {
        return noticeService.getNoticeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NoticeDto> createNotice(@RequestBody NoticeDto dto) {
        return ResponseEntity.ok(noticeService.createNotice(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeDto> updateNotice(@PathVariable String id, @RequestBody NoticeDto dto) {
        return noticeService.updateNotice(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable String id) {
        if (noticeService.deleteNotice(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
