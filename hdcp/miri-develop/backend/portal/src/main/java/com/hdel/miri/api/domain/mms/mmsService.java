package com.hdel.miri.api.domain.mms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hdel.miri.api.domain.scrm.SCRMRepository;

@Service
@RequiredArgsConstructor
public class mmsService {
    private final SCRMRepository scrmRepository;

    public int sendMMS(mmsVO.request request){
        return scrmRepository.insertSendMMS(request);
    }
}
