package com.example.baseballprediction.domain.member.controller;

import static com.example.baseballprediction.domain.member.dto.MemberRequest.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.baseballprediction.domain.member.dto.ProfileProjection;
import com.example.baseballprediction.domain.member.service.MemberService;
import com.example.baseballprediction.global.security.auth.JwtTokenProvider;
import com.example.baseballprediction.global.security.auth.MemberDetails;
import com.example.baseballprediction.global.util.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
		Map<String, Object> response = memberService.login(loginDTO.getUsername(), loginDTO.getPassword());

		return ResponseEntity
			.ok()
			.header(JwtTokenProvider.HEADER, (String)response.get("token"))
			.body("정상적으로 처리되었습니다.");
	}

	@PutMapping("/profile/team")
	public ResponseEntity<ApiResponse<?>> likeTeamModify(@RequestBody LikeTeamDTO likeTeamDTO,
		@AuthenticationPrincipal MemberDetails memberDetails) {
		memberService.modifyLikeTeam(memberDetails.getUsername(), likeTeamDTO.getTeamId());

		return ResponseEntity.ok(ApiResponse.createSuccess());
	}

	@PutMapping("/profile/details")
	public ResponseEntity<ApiResponse<?>> detailsModify(@RequestBody DetailsDTO detailsDTO,
		@AuthenticationPrincipal MemberDetails memberDetails) {

		memberService.modifyDetails(memberDetails.getUsername(), detailsDTO);

		return ResponseEntity.ok(ApiResponse.createSuccess());
	}

	@GetMapping("/profiles/{memberId}")
	public ResponseEntity<ApiResponse<ProfileProjection>> profileList(@PathVariable Long memberId) {
		ProfileProjection profile = memberService.findProfile(memberId);

		return ResponseEntity.ok(ApiResponse.success(profile));
	}
}
