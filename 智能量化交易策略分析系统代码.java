package com.brtpawn.continuePawn.house.controller;

import com.brtpawn.continuePawn.house.service.HouseContinuePawnService;
import com.brtpawn.controller.base.BaseController;
import com.brtpawn.controller.wxjk.BrtWxJk;
import com.brtpawn.controller.wxjk.BrtWxService;
import com.brtpawn.crm.CrmService;
import com.brtpawn.entity.system.User;
import com.brtpawn.service.brtpawn.accmgr.YwManagerService;
import com.brtpawn.service.brtpawn.common.Util;
import com.brtpawn.service.brtpawn.system.house.HouseTicketManagementService;
import com.brtpawn.util.Const;
import com.brtpawn.util.PageData;
import com.brtpawn.util.Tools;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * 财务审计--控制类
 * 
 * 
 * 
 */
@Controller
@RequestMapping(value = "/houseContinuePawnController")
public class HouseContinuePawnController extends BaseController {
	@Resource(name = "houseContinuePawnService")
	private HouseContinuePawnService houseContinuePawnService;
	@Resource(name = "houseTicketManagementService")
	private HouseTicketManagementService houseTicketManagementService;
	@Resource(name = "ywManagerService")
	private YwManagerService ywManagerService;
	@Resource(name = "crmService")
	private CrmService crmService;
	@Resource(name = "brtWxJk")
	private BrtWxJk brtWxJk;
	@Resource(name ="brtWxService")
	private BrtWxService brtWxService;
	/**
	 * 当票管理列表，点击 “续当” 按钮，获取相关数据，弹出收款通知书页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/continuePawn")
	public ModelAndView continuePawn(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		PageData ticketInfo = new PageData();
		// 获取界面参数
		pd = this.getPageData();
		// 获取票据信息
		ticketInfo = houseContinuePawnService.getTicketInfo(pd);
		// 收款通知单页面
		mv.setViewName("brtpawn/continuePawn/house/receiveNotification");
		// 获取区域后缀
		String areaCode = new Util().getAreaCode();
		String areaCodeSuffix;
		if ("02".equals(areaCode)) areaCodeSuffix = "_tianjin";
		else if ("03".equals(areaCode)) areaCodeSuffix = "_hangzhou";
		else if ("04".equals(areaCode)) areaCodeSuffix = "_hainan";
		else if ("05".equals(areaCode)) areaCodeSuffix = "_shanghai";
		else if ("06".equals(areaCode)) areaCodeSuffix = "_ningbo";
		else if ("07".equals(areaCode)) areaCodeSuffix = "_shenzhen";
		else areaCodeSuffix = "";
		mv.addObject("areaCodeSuffix", areaCodeSuffix);
		 
		mv.addObject("ticketInfo", ticketInfo);
		return mv;
	}
	
	@RequestMapping(value = "/continuePawn_fmp")
	public ModelAndView continuePawn_fmp(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		PageData ticketInfo = new PageData();
		// 获取界面参数
		pd = this.getPageData();
		// 获取票据信息
		ticketInfo = houseContinuePawnService.getTicketInfo(pd);
		// 收款通知单页面
		mv.setViewName("brtpawn/continuePawn/house/receiveNotification_fmp");
		// 获取区域后缀
		String areaCode = new Util().getAreaCode();
		String areaCodeSuffix;
		if ("02".equals(areaCode)) areaCodeSuffix = "_tianjin";
		else if ("03".equals(areaCode)) areaCodeSuffix = "_hangzhou";
		else if ("04".equals(areaCode)) areaCodeSuffix = "_hainan";
		else if ("05".equals(areaCode)) areaCodeSuffix = "_shanghai";
		else if ("06".equals(areaCode)) areaCodeSuffix = "_ningbo";
		else if ("07".equals(areaCode)) areaCodeSuffix = "_shenzhen";
		else areaCodeSuffix = "";
		mv.addObject("areaCodeSuffix", areaCodeSuffix);
		
		mv.addObject("ticketInfo", ticketInfo);
		return mv;
	}
	/**
	 * 计算实付金额
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/calDelay")
	@ResponseBody
	public PageData calDelay(HttpServletRequest request) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData resultData = houseContinuePawnService.cal(pd);
		return resultData;
	}
	/**
	 * 暂存收款通知单信息
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveSktzd")
	@ResponseBody
	public PageData saveSktzd(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		PageData sktzd = houseContinuePawnService.saveRecNoti(pd);
		return sktzd;
	}
	/**
	 * 提交财务
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitToFinance")
	public ModelAndView submitToFinance(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		houseContinuePawnService.submitToFinance(this.getLoginUserName(), pd);
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 出纳复核界面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/financeCheck")
	public ModelAndView financeCheck(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		PageData ticketInfo = houseContinuePawnService.getTicketInfo(pd);
		ticketInfo.put("loginUser", this.getLoginName());
		ticketInfo.put("CN", Tools.getUserId());
		mv.addObject("ticketInfo", ticketInfo);
		pd.put("firereport", Const.MY_FIREREPORTPATH);
		mv.addObject("pd10", pd);
		// 获取区域后缀
		String areaCode = new Util().getAreaCode();
		String areaCodeSuffix;
		if ("02".equals(areaCode)) areaCodeSuffix = "_tianjin";
		else if ("03".equals(areaCode)) areaCodeSuffix = "_hangzhou";
		else if ("04".equals(areaCode)) areaCodeSuffix = "_hainan";
		else if ("05".equals(areaCode)) areaCodeSuffix = "_shanghai";
		else if ("06".equals(areaCode)) areaCodeSuffix = "_ningbo";
		else if ("07".equals(areaCode)) areaCodeSuffix = "_shenzhen";
		else areaCodeSuffix = "";
		mv.addObject("areaCodeSuffix", areaCodeSuffix);
		mv.setViewName("brtpawn/continuePawn/house/financeCheck");
		return mv;
	}
	/**
	 * 出纳复核通过
	 */
	@RequestMapping(value = "/agree")
	public ModelAndView agree(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		PageData returnInfo=houseContinuePawnService.agree(this.getLoginUserName(), pd);
		//判断是否是外阜 如果是外阜 则不执行下面操作
		String xmbh = returnInfo.getString("XMBH");
		pd.put("xmbh", xmbh);
		String areaCode = ywManagerService.getAreaCodeByXmbh(pd);
		if("01".equals(areaCode)){
			crmService.crm_XudangFanli(returnInfo);
		}
		String xdlx=returnInfo.getString("XDLX");
		if("02".equals(xdlx)){
			returnInfo.put("PJBH", returnInfo.getString("SDPJBH"));
			//判断是否是外阜 如果是外阜 则不执行下面操作
			if("01".equals(areaCode)){
				crmService.CRM_Shudangfanli(returnInfo);
			}
		}
		if("03".equals(pd.getString("XDFS"))){
			String pawnStatus  = "04";
			String pawnNo  = pd.getString("LRDPH");
			String originalPawnNo = pd.getString("YDPH");
			String ret = HouseContinuePawnService.sendSms(pawnStatus,pawnNo,originalPawnNo);
			List<PageData> weixinStatus = brtWxService.getWeixinStatus(pd);//查询状态为0的票是否存在
			PageData changeStatus = new PageData();
			changeStatus.put("ID",weixinStatus.get(0).getString("ID"));
			changeStatus.put("XGR", this.getLoginUserName());
			brtWxService.updateStatus(changeStatus);
			System.out.println(ret+"==============\n\n\n\n\n\n");
		}
		List<PageData> weixinStatus = brtWxService.getWeixinStatus(pd);//查询状态为0的票是否存在
		if(weixinStatus.size()>0){
			String pawnStatus  = "04";
			String pawnNo  = pd.getString("LRDPH");
			String originalPawnNo = pd.getString("YDPH");
			String ret = HouseContinuePawnService.sendSms(pawnStatus,pawnNo,originalPawnNo);
			//System.out.println(ret+"==============\n\n\n\n\n\n");
			PageData changeStatus = new PageData();
			changeStatus.put("ID",weixinStatus.get(0).getString("ID"));
			changeStatus.put("XGR", this.getLoginUserName());
			brtWxService.updateStatus(changeStatus);
		}
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 出纳回退
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reject")
	public ModelAndView reject(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		houseContinuePawnService.reject(this.getLoginUserName(), pd);
		List<PageData> weixinStatus = brtWxService.getWeixinStatus(pd);//查询状态为0的票是否存在
		if(weixinStatus.size()>0){
			String pawnStatus  = "96";
			String pawnNo  ="";
			String originalPawnNo = pd.getString("YDPH");
			String ret = HouseContinuePawnService.sendSms(pawnStatus,pawnNo,originalPawnNo);
		}
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 判断当票号是否重复
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/judgeDph")
	@ResponseBody
	public String judgeDph(HttpServletRequest request) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		String s = houseContinuePawnService.judgeDph(pd);
		return s;
	}
	/**
	 * 获取当前登录用户用户名
	 * 
	 * @return
	 */
	public String getLoginUserName() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		String userName = user.getUSERNAME();
		return userName;
	}
	/**
	 * 获取当前登录用户中文名
	 */
	public String getLoginName() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		String name = user.getNAME();
		return name;
	}
	/**
	 * 
	 */
	/**
	 *保存当票打印状态
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveSdSFDY")
	@ResponseBody
	public void saveSdSFDY(HttpServletRequest request) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
	    houseContinuePawnService.saveSdSFDY(pd);
		
	}
	/**
	 *保存当票打印状态
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveXdSFDY")
	@ResponseBody
	public void saveXdSFDY(HttpServletRequest request) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
	    houseContinuePawnService.saveXdSFDY(pd);
		
	}
	/***
	 *续当终止
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stopContinue")
	@ResponseBody
	public String stopContinue(HttpServletRequest request) throws Exception {
		PageData pd = new PageData();
		String s="0";
		pd = this.getPageData();
	    houseContinuePawnService.stopContinue(pd);
	    s="1";
		return s;
	}
	
	/***
	 *续当取消逾期减免
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancelReduce")
	@ResponseBody
	public String cancelReduce(HttpServletRequest request) throws Exception {
		PageData pd = new PageData();
		String s="0";
		pd = this.getPageData();
	    houseContinuePawnService.cancelReduce(pd);
	    s="1";
		return s;
	}
	
	@RequestMapping(value="/getHandleIdentity")
	@ResponseBody
	public PageData getDealDisabled(HttpServletRequest request) throws Exception{
		PageData returnInfo=new PageData();
		PageData pd = new PageData();
		pd = this.getPageData();
		returnInfo=houseTicketManagementService.getHandleIdentity(pd);
		
	return returnInfo;
	}
	
	
}

package com.brtpawn.controller.brtpawn.civilPawn;
import com.brtpawn.continuePawn.house.service.HouseContinuePawnService;
import com.brtpawn.controller.base.BaseController;
import com.brtpawn.controller.wxjk.BrtWxService;
import com.brtpawn.crm.CrmService;
import com.brtpawn.service.brtpawn.accmgr.YwManagerService;
import com.brtpawn.service.brtpawn.common.Util;
import com.brtpawn.service.brtpawn.cvivilPawn.CivilContinuePawnService;
import com.brtpawn.util.Const;
import com.brtpawn.util.PageData;
import com.brtpawn.util.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value="/civilContinue")
public class CivilContinuePawnController extends BaseController {
	
	
	@Resource(name = "civilContinuePawnService")
	private CivilContinuePawnService civilContinuePawnService;
	@Resource(name = "houseContinuePawnService")
	private HouseContinuePawnService houseContinuePawnService;
	@Resource(name = "ywManagerService")
	private YwManagerService ywManagerService;
	
	@Resource(name = "crmService")
	private CrmService crmService;
	@Resource(name ="brtWxService")
	private BrtWxService brtWxService;
	/**
	 
	 * @param request
	 * @return
	 * @throws Exception
	 */
		@RequestMapping(value = "/continuePawn")
		public ModelAndView continuePawn(HttpServletRequest request) throws Exception {
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			PageData ticketInfo = new PageData();
			// 获取界面参数
			pd = this.getPageData();
			// 获取票据信息
			ticketInfo = civilContinuePawnService.getCivilTicketInfo(pd);
			// 获取区域后缀
			String areaCode = new Util().getAreaCode();
			String areaCodeSuffix;
			if ("02".equals(areaCode)) areaCodeSuffix = "_tianjin";
			else if ("03".equals(areaCode)) areaCodeSuffix = "_hangzhou";
			else if ("04".equals(areaCode)) areaCodeSuffix = "_hainan";
			else if ("05".equals(areaCode)) areaCodeSuffix = "_shanghai";
			else if ("06".equals(areaCode)) areaCodeSuffix = "_ningbo";
			else if ("07".equals(areaCode)) areaCodeSuffix = "_shenzhen";
			else areaCodeSuffix = "";
			mv.addObject("areaCodeSuffix", areaCodeSuffix);
			// 收款通知单页面
			mv.setViewName("brtpawn/continuePawn/civilian/civilReceiveNotification");
			// 票据信息
			mv.addObject("ticketInfo", ticketInfo);
			mv.addObject("pd", pd);
			return mv;
		}
		
		//续当减免页面回显信息
		@RequestMapping(value = "/continuePawn_mp")
		public ModelAndView continuePawn_mp(HttpServletRequest request) throws Exception {
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			PageData ticketInfo = new PageData();
			// 获取界面参数
			pd = this.getPageData();
			// 获取票据信息
			ticketInfo = civilContinuePawnService.getCivilTicketInfo(pd);
			// 获取区域后缀
			String areaCode = new Util().getAreaCode();
			String areaCodeSuffix;
			if ("02".equals(areaCode)) areaCodeSuffix = "_tianjin";
			else if ("03".equals(areaCode)) areaCodeSuffix = "_hangzhou";
			else if ("04".equals(areaCode)) areaCodeSuffix = "_hainan";
			else if ("05".equals(areaCode)) areaCodeSuffix = "_shanghai";
			else if ("06".equals(areaCode)) areaCodeSuffix = "_ningbo";
			else if ("07".equals(areaCode)) areaCodeSuffix = "_shenzhen";
			else areaCodeSuffix = "";
			mv.addObject("areaCodeSuffix", areaCodeSuffix);
			// 收款通知单页面
			mv.setViewName("brtpawn/continuePawn/civilian/civilReceiveNotification_mp");
			// 票据信息
			mv.addObject("ticketInfo", ticketInfo);
			mv.addObject("pd", pd);
			return mv;
		}
		/***
		 *续当终止 by zy -01-10
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/stopContinue")
		@ResponseBody
		public String stopContinue(HttpServletRequest request) throws Exception {
			PageData pd = new PageData();
			String s="0";
			pd = this.getPageData();
			civilContinuePawnService.stopContinue(pd);
		    s="1";
			return s;
		}
		
		
		
		/**
		 * 计算实付金额
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/civilCalDelay")
		@ResponseBody
		public PageData calDelay(HttpServletRequest request) throws Exception {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData resultData = civilContinuePawnService.civilCal(pd);
			return resultData;
		}
		
		/**
		 * 暂存收款通知单信息
		 * 
		 * @throws Exception
		 */
		@RequestMapping(value = "/saveCivilSktzd")
		@ResponseBody
		public PageData saveSktzd(HttpServletRequest request) throws Exception {
		
			PageData pd = this.getPageData();
			PageData sktzd = civilContinuePawnService.saveCivilRecNoti(pd);
			return sktzd;
		}
		/**
		 * 提交财务
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/submitToTeller")
		public ModelAndView submitToFinance(HttpServletRequest request) throws Exception {
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			civilContinuePawnService.submitToTeller(Tools.getUsername(), pd);
			mv.setViewName("save_result");
			return mv;
		}
		
		
		/**
		 * 提交审批人员进行审批
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/submitToApprover")
		@ResponseBody
		public String submitToApprover(HttpServletRequest request) throws Exception {
			PageData pd = this.getPageData();
			
			String flag=civilContinuePawnService.submitToApprover(Tools.getUsername(), pd);
			
			return flag;
		}
		/**
		 * 出纳复核界面
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/TellerCheck")
		public ModelAndView financeCheck(HttpServletRequest request) throws Exception {
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			PageData ticketInfo = civilContinuePawnService.getCivilTicketInfo(pd);
			ticketInfo.put("loginUser", Tools.getUsername());
			ticketInfo.put("CN", Tools.getUserId());
			mv.addObject("ticketInfo", ticketInfo);
			pd.put("firereport", Const.MY_FIREREPORTPATH);
			mv.addObject("pd10", pd);
			// 获取区域后缀
			String areaCode = new Util().getAreaCode();
			String areaCodeSuffix;
			if ("02".equals(areaCode)) areaCodeSuffix = "_tianjin";
			else if ("03".equals(areaCode)) areaCodeSuffix = "_hangzhou";
			else if ("04".equals(areaCode)) areaCodeSuffix = "_hainan";
			else if ("05".equals(areaCode)) areaCodeSuffix = "_shanghai";
			else if ("06".equals(areaCode)) areaCodeSuffix = "_ningbo";
			else if ("07".equals(areaCode)) areaCodeSuffix = "_shenzhen";
			else areaCodeSuffix = "";
			mv.addObject("areaCodeSuffix", areaCodeSuffix);
			mv.setViewName("brtpawn/continuePawn/civilian/civilFinanceCheck");
			return mv;
		}
		/**
		 * 出纳复核通过
		 */
		@RequestMapping(value = "/civilAgree")
		public ModelAndView agree(HttpServletRequest request) throws Exception {
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			PageData returnInfo=civilContinuePawnService.civilAgree(Tools.getUsername(), pd);
			crmService.crm_XudangFanli(returnInfo);
			String xdlx=returnInfo.getString("XDLX");
			//判断是否是外阜 如果是外阜 则不执行下面操作
			String xmbh = returnInfo.getString("XMBH");
			pd.put("xmbh", xmbh);
			String areaCode = ywManagerService.getAreaCodeByXmbh(pd);
			if("02".equals(xdlx)){
				returnInfo.put("PJBH", returnInfo.getString("SDPJBH"));
				//判断是否是外阜 如果是外阜 则不执行下面操作
				if("01".equals(areaCode)){
					crmService.CRM_Shudangfanli(returnInfo);
				}
			}
			if("03".equals(pd.getString("XDFS"))){
				String pawnStatus  = "04";
				String pawnNo  = pd.getString("LRDPH");
				String originalPawnNo = pd.getString("YDPH");
				String ret = HouseContinuePawnService.sendSms(pawnStatus,pawnNo,originalPawnNo);
				List<PageData> weixinStatus = brtWxService.getWeixinStatus(pd);
				PageData changeStatus = new PageData();
				changeStatus.put("ID",weixinStatus.get(0).getString("ID"));
				brtWxService.updateStatus(changeStatus);
				System.out.println(ret+"==============\n\n\n\n\n\n");
			}
			List<PageData> weixinStatus = brtWxService.getWeixinStatus(pd);
			if(weixinStatus.size()>0){
				String pawnStatus  = "04";
				String pawnNo  = pd.getString("LRDPH");
				String originalPawnNo = pd.getString("YDPH");
				String ret = HouseContinuePawnService.sendSms(pawnStatus,pawnNo,originalPawnNo);
				//System.out.println(ret+"==============\n\n\n\n\n\n");
				PageData changeStatus = new PageData();
				changeStatus.put("ID",weixinStatus.get(0).getString("ID"));
				brtWxService.updateStatus(changeStatus);
			}
			mv.setViewName("save_result");
			return mv;
		}
		/**
		 * 出纳回退
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/civilReject")
		public ModelAndView reject(HttpServletRequest request) throws Exception {
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			civilContinuePawnService.civilReject(Tools.getUsername(), pd);
			List<PageData> weixinStatus = brtWxService.getWeixinStatus(pd);//查询状态为0的票是否存在
			if(weixinStatus.size()>0){
				String pawnStatus  = "96";
				String pawnNo  ="";
				String originalPawnNo = pd.getString("YDPH");
				String ret = HouseContinuePawnService.sendSms(pawnStatus,pawnNo,originalPawnNo);
			}
			mv.setViewName("save_result");
			return mv;
		}
		/**
		 * 进入审批人员审批页面
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */ 
		@RequestMapping(value="/civilApprove")
		public ModelAndView civilApprove(HttpServletRequest request) throws Exception {
			
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			PageData ticketInfo = civilContinuePawnService.getCivilApproveInfo(pd);
			ticketInfo.put("loginUser", Tools.getUsername());
			mv.addObject("ticketInfo", ticketInfo);
			mv.setViewName("brtpawn/continuePawn/civilian/civilContinueApprove");
			return mv;
		}
		
		
		/**
		 * 审批人员审批(若取走当物)
		 * @param requst
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/approveSumbit")
		public ModelAndView approveSumbit(HttpServletRequest requst)throws Exception
		{
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			civilContinuePawnService.approveSumbit(Tools.getUsername(), pd);
			mv.setViewName("save_result");
			return mv;
		}
		/**
		 * 库管进入出库页面(若取走当物)
		 * @param requst
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/storageCheck")
		public ModelAndView storageCheck(HttpServletRequest requst)throws Exception
		{
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			PageData ticketInfo =civilContinuePawnService.storageCheck(pd);
			mv.addObject("ticketInfo", ticketInfo);
			mv.setViewName("brtpawn/continuePawn/civilian/civilStorageCheck");
			return mv;
		}
		/**
		 * 库管出库意见提交(取走当物，库管进行出库提交)
		 * @param requst
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/storageAgree")
		public ModelAndView storageSumbit(HttpServletRequest requst)throws Exception
		{
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			civilContinuePawnService.storageAgree(pd);
			mv.setViewName("save_result");
			return mv;
		}
		
		/**
		 * 库管出库意见提交(取走当物，库管进行出库提交)
		 * @param requst
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/storageReject")
		public ModelAndView storageReject(HttpServletRequest requst)throws Exception
		{
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			civilContinuePawnService.storageReject(pd);
			mv.setViewName("save_result");
			return mv;
		}
		@RequestMapping(value = "/checkMpmx")
		@ResponseBody
		public String checkMpmx(HttpServletRequest request) throws Exception {
			PageData pd = this.getPageData();
			String result = "0";
			List<PageData> checkMpmx=civilContinuePawnService.checkMpmx(pd);
			if(checkMpmx.size() > 0 ){
				result = "1";
			}
			return result;
		}
		 
}

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>