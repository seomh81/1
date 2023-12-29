package com.hdel.miri.concurrent.domain.scrm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SCRMService {
    private final SCRMRepository scrmRepository;

    public List<SCRM.VO> getInternalKeysFromSCRM(List data) {
        return scrmRepository.getInternalKeysFromSCRM(data);
    }
}
