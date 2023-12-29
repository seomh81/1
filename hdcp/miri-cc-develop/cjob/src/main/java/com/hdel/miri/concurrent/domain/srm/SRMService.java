package com.hdel.miri.concurrent.domain.srm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.hdel.miri.concurrent.domain.scrm.SCRM;

import java.util.List;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SRMService {

    private final SRMRepository srmRepository;

    public List<SCRM.VO> getInternalKeysFromSRM(List<SCRM.VO> data) {
        return srmRepository.getInternalKeysFromSRM(data);
    }
}
