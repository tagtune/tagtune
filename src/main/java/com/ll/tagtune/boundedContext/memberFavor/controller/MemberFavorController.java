package com.ll.tagtune.boundedContext.memberFavor.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.memberFavor.dto.FavorTagDTO;
import com.ll.tagtune.boundedContext.memberFavor.service.FavorService;
import com.ll.tagtune.boundedContext.tag.dto.TagDTO;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/favor")
@RequiredArgsConstructor
public class MemberFavorController {
    private final FavorService favorService;
    private final TagService tagService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model) {
        List<FavorTagDTO> favorTags = favorService.getFavorTags(rq.getMember().getId())
                .stream()
                .map(tag -> FavorTagDTO.builder()
                        .id(tag.getId())
                        .tagId(tag.getData().getId())
                        .name(tag.getData().getTagName())
                        .build()
                )
                .toList();

        model.addAttribute("favorTags", favorTags);

        return "usr/favor/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addTag")
    public String addFavorTag(TagDTO tagDTO) {
        Optional<Tag> optionalTag = tagService.findByName(tagDTO.getTagName());
        if (optionalTag.isEmpty()) return rq.redirectWithMsg("/favor/list", "존재하지 않는 태그입니다.");
        favorService.create(rq.getMember(), optionalTag.get());

        return rq.redirectWithMsg("/favor/list", "태그가 추가되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{favorTagId}")
    public String deleteFavorTag(@PathVariable Long favorTagId) {
        RsData<Void> rsData = favorService.delete(rq.getMember(), favorTagId);
        if (rsData.isFail()) return rq.historyBack(rsData);

        return rq.redirectWithMsg("/favor/list", "태그가 삭제되었습니다.");
    }
}
