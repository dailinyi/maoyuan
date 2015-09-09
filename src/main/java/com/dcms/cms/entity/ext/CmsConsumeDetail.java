package com.dcms.cms.entity.ext;

import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.statistic.ScoreUtils;

/**
 * Created by Daily on 2015/9/9.
 */
public class CmsConsumeDetail {
    private CmsUser buyer;
    private CmsUser seller;
    /* 消费金额 */
    private Integer price;
    /* 使用积分 */
    private Integer useScore;
    /* 返还积分 */
    private Integer returnScore;
    /* 返还推荐人积分 */
    private Integer returnSellerScore;




    public CmsUser getBuyer() {
        return buyer;
    }

    public CmsConsumeDetail setBuyer(CmsUser buyer) {
        this.buyer = buyer;
        return this;
    }

    public CmsUser getSeller() {
        return seller;
    }

    public CmsConsumeDetail setSeller(CmsUser seller) {
        this.seller = seller;
        return this;
    }

    public Integer getPrice() {
        return price == null ? 0 : price;
    }

    public CmsConsumeDetail setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public Integer getUseScore() {
        return useScore == null ? 0 : useScore;
    }

    public CmsConsumeDetail setUseScore(Integer useScore) {
        this.useScore = useScore;
        return this;
    }

    public Integer getReturnScore() {
        return returnScore == null ? 0 : returnScore;
    }

    public CmsConsumeDetail setReturnScore(Integer returnScore) {
        this.returnScore = returnScore;
        return this;
    }

    public Integer getReturnSellerScore() {
        return returnSellerScore == null ? 0 : returnSellerScore;
    }

    public CmsConsumeDetail setReturnSellerScore(Integer returnSellerScore) {
        this.returnSellerScore = returnSellerScore;
        return this;
    }

    public String getReturnScoreShow() {
        return ScoreUtils.intToStr(this.getReturnScore());
    }
    public String getReturnSellerScoreShow() {
        return ScoreUtils.intToStr(this.getReturnSellerScore());
    }
    public String getUseScoreShow() {
        return ScoreUtils.intToStr(this.getUseScore());
    }
    public String getPriceShow() {
        return ScoreUtils.intToStr(this.getPrice());
    }

    @Override
    public String toString() {
        return "CmsConsumeDetail{" +
                "buyer=" + buyer +
                ", seller=" + seller +
                ", price=" + price +
                ", useScore=" + useScore +
                ", returnScore=" + returnScore +
                ", returnSellerScore=" + returnSellerScore +
                '}';
    }
}
