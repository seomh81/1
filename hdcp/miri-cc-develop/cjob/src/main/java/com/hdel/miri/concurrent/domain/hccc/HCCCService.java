package com.hdel.miri.concurrent.domain.hccc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.hdel.miri.concurrent.domain.scrm.SCRM;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HCCCService {

    private final HCCCRepository hcccRepository;

    public List<SCRM.VO> getGisFromHccc(List data){
        return hcccRepository.getGisFromHccc(data);
    }
}
