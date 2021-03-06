package com.dcms.cms.manager.main;

import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.CmsUserExt;
import com.dcms.common.email.EmailSender;
import com.dcms.common.email.MessageTemplate;
import com.dcms.common.page.Pagination;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface CmsUserMng {
	public Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize);
	
	public List<CmsUser> getList(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank);

	public List<CmsUser> getAdminList(Integer siteId, Boolean allChannel,
			Boolean disabled, Integer rank);

	public CmsUser findById(Integer id);

	public CmsUser findByUsername(String username);

	CmsUser registerMember(String username, String email,
						   String password, String ip, Integer groupId, CmsUserExt userExt, CmsUser recommendUser);

	CmsUser updateQr(Integer userId, String userQR, String shopQR, String shopCode);

	CmsUser updateEmail(Integer userId, String email);

	public CmsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, CmsUserExt userExt);
	
	public CmsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, CmsUserExt userExt, Boolean activation , EmailSender sender, MessageTemplate msgTpl)throws UnsupportedEncodingException, MessagingException ;

	public void updateLoginInfo(Integer userId, String ip);

	public void updateUploadSize(Integer userId, Integer size);
	
	public void updateUser(CmsUser user);

	public void updatePwdEmail(Integer id, String password, String email);

	public boolean isPasswordValid(Integer id, String password);

	public CmsUser saveAdmin(String username, String email, String password,
			String ip, boolean viewOnly, boolean selfAdmin, int rank,
			Integer groupId, Integer[] roleIds, Integer[] channelIds,
			Integer[] siteIds, Byte[] steps, Boolean[] allChannels,
			CmsUserExt userExt);

	public CmsUser updateAdmin(CmsUser bean, CmsUserExt ext, String password,
			Integer groupId, Integer[] roleIds, Integer[] channelIds,
			Integer[] siteIds, Byte[] steps, Boolean[] allChannels);

	public CmsUser updateAdmin(CmsUser bean, CmsUserExt ext, String password,
			Integer groupId, Integer[] roleIds, Integer[] channelIds,
			Integer siteId, Byte step, Boolean allChannel);

	public CmsUser updateMember(Integer id, String email, String password,
			Boolean isDisabled, CmsUserExt ext, Integer groupId);

	CmsUser updateMemberScore(Integer id, Integer score);

	CmsUser updateMember(Integer id, String email, String password,
						 Boolean isDisabled, CmsUserExt ext, Integer groupId, Byte rate);

	public CmsUser updateUserConllection(CmsUser user,Integer cid,Integer operate);

	public void addSiteToUser(CmsUser user, CmsSite site, Byte checkStep);

	public CmsUser deleteById(Integer id);

	public CmsUser[] deleteByIds(Integer[] ids);

	CmsUser getUserByRecommendCode(String recommendCode);

	public boolean usernameNotExist(String username);
	
	public boolean usernameNotExistInMember(String username);

	public boolean emailNotExist(String email);

	void updatePwd(Integer id, String newPwd);

	Pagination getRecommendUserPage(String userName, Integer userId, int pageNo, int pageSize);
}