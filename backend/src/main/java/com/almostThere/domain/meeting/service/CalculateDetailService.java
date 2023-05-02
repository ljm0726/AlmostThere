package com.almostThere.domain.meeting.service;

import com.almostThere.domain.meeting.dto.ReceiptResponseDto;
import com.almostThere.domain.meeting.dto.create.CalculateDetailRequestDto;
import com.almostThere.domain.meeting.entity.CalculateDetail;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.meeting.repository.CalculateDetailRepository;
import com.almostThere.domain.meeting.repository.MeetingMemberRepository;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import com.almostThere.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CalculateDetailService {
    private final CalculateDetailRepository calculateDetailRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    public ReceiptResponseDto parseData(StringBuffer response) throws ParseException {
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject object = (org.json.simple.JSONObject)parser.parse(response.toString());
        org.json.simple.JSONObject all = (org.json.simple.JSONObject) ((org.json.simple.JSONArray) object.get("images")).get(0);
        org.json.simple.JSONObject receipt = (org.json.simple.JSONObject) ((org.json.simple.JSONObject) all.get("receipt")).get("result");
        org.json.simple.JSONObject storeInfo = ((org.json.simple.JSONObject) receipt.get("storeInfo"));
        org.json.simple.JSONObject totalPrice = (org.json.simple.JSONObject)receipt.get("totalPrice");
        storeInfo = (org.json.simple.JSONObject) ((org.json.simple.JSONObject)storeInfo.get("name")).get("formatted");
        totalPrice = (org.json.simple.JSONObject) ((org.json.simple.JSONObject)totalPrice.get("price")).get("formatted");
        ReceiptResponseDto result = new ReceiptResponseDto();
        result.setStoreName(storeInfo.get("value").toString());
        result.setTotalPrice( Integer.valueOf((String) totalPrice.get("value")));
        return result;
    }

    @Transactional
    public void saveCalculateDetail(CalculateDetailRequestDto dto) throws IOException {

        Meeting meeting = meetingRepository.findById(dto.getMeetingId()).orElseThrow();
        //영수증 파일 저장
        String hostname = InetAddress.getLocalHost().getHostName();
        FileUtil fileUtil = new FileUtil();
        HashMap<String, String> file = fileUtil.fileCreate(hostname,dto.getReceipt());
        //CalculateDetail 저장
        CalculateDetail calculateDetail = new CalculateDetail(dto, file.get("filePath"), file.get("fileName"), meeting);
        calculateDetailRepository.save(calculateDetail);

        List<MeetingMember> members = meeting.getMeetingMembers();  //모임의 총 인원
        List<MeetingMember> lates = meetingMemberRepository.findByMeetingIdAndState(dto.getMeetingId(), StateType.LATE); //지각한 인원
        Integer lateAmount = meeting.getLateAmount();   //지각비
        int totalPrice = calculateDetailRepository.sumMeetingPrice(dto.getMeetingId());
        int memberCnt = members.size();

        if(lateAmount!=null)
            totalPrice -= lateAmount*lates.size();

        int per = 0;
        int remain = 0;
//        System.out.println("총금액: "+totalPrice+" 모임인원수: "+memberCnt);
//        System.out.println("나머지: "+(totalPrice*1.0)%memberCnt);
//        System.out.println("인당금액: "+(int)(Math.floor((totalPrice/memberCnt)/10) * 10));

        //총금액을 모임인원으로 나눴을 때 나눠떨어지는 경우
        if((totalPrice*1.0)%memberCnt==0){
            per = totalPrice/memberCnt;
        }
        //나눠떨어지지 않는 경우
        else {
            System.out.println("나눠떨어지지 않습니다.");
            per = (int)(Math.floor((totalPrice/memberCnt)/10) * 10);
            remain = totalPrice - (per*memberCnt);
            System.out.println("인당금액: "+per);
        }

        for(MeetingMember m: members){
            //모임지각비 X, 지각 O 사람인 경우 -> 단순히 1/N
            if(lateAmount==null&&m.getState()==StateType.LATE)
                m.updateSpentMoney(per);
            //모임지각비 O, 지각 O 사람인 경우 -> 지각비 + 1/N
            else if(lateAmount!=null&&m.getState()==StateType.LATE)
                m.updateSpentMoney(lateAmount+per);
            else if(m.getState()==StateType.ARRIVE)
                m.updateSpentMoney(per);

            meetingMemberRepository.save(m);
        }
    }

}
