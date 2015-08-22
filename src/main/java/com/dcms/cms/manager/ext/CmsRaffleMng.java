package com.dcms.cms.manager.ext;

import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.Content;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.manager.main.ContentMng;

import javax.annotation.Resource;

public interface CmsRaffleMng {


    Content doEggRaffle(CmsUser user);
}