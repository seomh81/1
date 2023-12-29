package com.hdel.miri.concurrent.domain.mms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hdel.miri.concurrent.domain.scrm.SCRMRepository;

//2023-10-13 add
@Service
@RequiredArgsConstructor
public class mmsService {
    private final SCRMRepository scrmRepository;

    public int sendMMS(mmsVO.request request){
        return scrmRepository.insertSendMMS(request);
    }
}

