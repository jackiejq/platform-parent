package com.slljr.finance.admin.service;

import com.slljr.finance.common.pojo.model.UserBankCard;
import com.slljr.finance.common.utils.HideDataUtil;
import com.slljr.finance.common.utils.RSAUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static org.apache.catalina.manager.Constants.CHARSET;

@Service
public class RsaService {
    private static final Logger log = LogManager.getLogger();

    /**
     *RSA私公钥加密
     */
    @Value("${app.security.publicKey}")
    private String publicKey;

    /**
     * RSA私钥解密
     */
    @Value("${app.security.privateKey}")
    private String privateKey;

    /**
     * rsa加密
     * @param str
     * @return
     */
    public String encode(String str){
        //RSA公钥加密存数据库
        final Base64.Encoder encoder = Base64.getEncoder();
        try {
            byte[] encodeData1 = RSAUtil.encryptByPublicKey(str.getBytes(CHARSET), publicKey);
            byte[] encodedText = encoder.encode(encodeData1);
            return new String(encodedText);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * rsa解密
     * @param str
     * @return
     */
    public String decode(String str){
        final Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] decodeData1 = RSAUtil.decryptByPrivateKey(decoder.decode(str), privateKey);
            return new String(decodeData1);
        } catch (Exception e) {
            log.error("RsaService.decode解密失败：", e);
        }
        return null;
    }

    /**
     * 银行卡信息解密
     * @param bankCard
     */
    public void bankCardDecode(UserBankCard bankCard){
        if (bankCard == null) return;
        if (StringUtils.isNotBlank(bankCard.getBankCardNoSign()) && bankCard.getBankCardNoSign().length() > 25){
            bankCard.setBankCardNo(decode(bankCard.getBankCardNoSign()));
        }
        if (StringUtils.isNotBlank(bankCard.getCvvCode()) && bankCard.getCvvCode().length() > 5){
            bankCard.setCvvCode(decode(bankCard.getCvvCode()));
        }
    }

    /**
     * 银行卡信息加密
     * @param bankCard
     */
    public void bankCardEncode(UserBankCard bankCard){
        if (bankCard == null) return;
        String bankCardNo = bankCard.getBankCardNo();
        if (StringUtils.isNotBlank(bankCardNo) && bankCardNo.length() < 25){
            //隐藏后的明文, 显示前6位,中间*号, 后4位,
            bankCard.setBankCardNo(HideDataUtil.hidebankCard(bankCardNo));
            bankCard.setBankCardNoSign(encode(bankCardNo));
        }
        if (StringUtils.isNotBlank(bankCard.getCvvCode()) && bankCard.getCvvCode().length() ==3){
            bankCard.setCvvCode(encode(bankCard.getCvvCode()));
        }
    }

}
