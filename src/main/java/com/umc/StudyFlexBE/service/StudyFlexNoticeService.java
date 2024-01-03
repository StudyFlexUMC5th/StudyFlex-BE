package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.request.StudyFlexNoticeUploadDto;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.StudyFlexNoticeListResponseDto;
import com.umc.StudyFlexBE.dto.response.StudyFlexNoticeResponseDto;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.Notice;
import com.umc.StudyFlexBE.repository.MemberRepository;
import com.umc.StudyFlexBE.repository.StudyFlexNoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyFlexNoticeService {
    private final StudyFlexNoticeRepository StudyFlexNoticeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public StudyFlexNoticeService(StudyFlexNoticeRepository StudyFlexNoticeRepository, MemberRepository memberRepository) {
        this.StudyFlexNoticeRepository = StudyFlexNoticeRepository;
        this.memberRepository = memberRepository;
    }

    public Notice createNotice(StudyFlexNoticeUploadDto request) throws BaseException {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_SUCH_EMAIL));

        Notice notice = new Notice();
        notice.setMember(member);
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setView(0);

        return StudyFlexNoticeRepository.save(notice);
    }

    public StudyFlexNoticeResponseDto getNotice(Long noticeId) throws BaseException {
        Notice notice = StudyFlexNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.BAD_REQUEST)); //need to change err code

        return new StudyFlexNoticeResponseDto(
                notice.getId(),
                notice.getView(),
                notice.getTitle(),
                notice.getMember().getMemberId(),
                notice.getContent(),
                notice.getCreated_at(),
                notice.getUpdated_at()
        );
    }

    public StudyFlexNoticeListResponseDto getNoticeList(int page, int itemSize) {
        Pageable pageable = PageRequest.of(page, itemSize);
        Page<Notice> noticePage = StudyFlexNoticeRepository.findAll(pageable);

        List<StudyFlexNoticeListResponseDto.NoticeSummary> noticeSummaries = noticePage.getContent().stream()
                .map(notice -> new StudyFlexNoticeListResponseDto.NoticeSummary(
                        notice.getId(),
                        notice.getTitle(),
                        notice.getMember().getName(),
                        notice.getView()
                ))
                .collect(Collectors.toList());

        return new StudyFlexNoticeListResponseDto(
                page,
                itemSize,
                noticePage.getTotalPages(),
                noticeSummaries
        );
    }

    public StudyFlexNoticeListResponseDto searchNotices(String searchTerm, int page, int itemSize) {
        Pageable pageable = PageRequest.of(page, itemSize);
        Page<Notice> noticePage = StudyFlexNoticeRepository.findByTitleOrContentContaining(searchTerm, pageable);

        List<StudyFlexNoticeListResponseDto.NoticeSummary> noticeSummaries = noticePage.getContent().stream()
                .map(notice -> new StudyFlexNoticeListResponseDto.NoticeSummary(
                        notice.getId(),
                        notice.getTitle(),
                        notice.getMember().getName(),
                        notice.getView()
                ))
                .collect(Collectors.toList());

        return new StudyFlexNoticeListResponseDto(
                page,
                itemSize,
                noticePage.getTotalPages(),
                noticeSummaries
        );
    }

}

