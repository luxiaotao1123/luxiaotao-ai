package com.wuai.company.order.controller;

import static com.wuai.company.util.JwtToken.ID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.order.entity.TrystEvaluation;
import com.wuai.company.order.service.TrystEvaluationService;
import com.wuai.company.util.Response;

/**
 * 用户评价的controller
 */
@RestController
@RequestMapping("tryst")
public class TrystEvaluationController {
	
	@Autowired
    private TrystEvaluationService trystEvaluationService;

    /**
     * 添加评价
     * @param req
     * @param targetUserId
     * @param type
     * @param serviceAttitude
     * @param serviceLabel
     * @param similarity
     * @param similarityLabel
     * @return
     */
	@PostMapping("evaluation/create/auth")
    public Response createTrystEvaluation(HttpServletRequest req,String targetUserId,Integer type,String serviceAttitude,String serviceLabel,
			String similarity,String similarityLabel,String trystId) {
		//1369070609269248896941  (String)req.getAttribute(ID)
        
        return trystEvaluationService.createEvaluation((Integer)req.getAttribute(ID), targetUserId, type, serviceAttitude, serviceLabel, similarity, similarityLabel,trystId);

    }
	
	/**
	 * 删除评价
	 * @param req
	 * @return
	 */
	@PostMapping("evaluation/del/auth")
	public Response deleteEvaluation(HttpServletRequest req,String uuid){
		
		return trystEvaluationService.deleteEvaluation((Integer) req.getAttribute(ID), uuid);
		
	}
	
	/**
	 * 获取自身关于指定订单的评价
	 * @param trystId
	 * @param req
	 * @return
	 */
	@PostMapping("evaluation/find/auth")
	public Response selectEvaluationByTrystId(String trystId, HttpServletRequest req){
		//(Integer) req.getAttribute(ID)
		return trystEvaluationService.selectEvaluationByTrystId(trystId, (Integer) req.getAttribute(ID));
		
	}
	
	 
}
