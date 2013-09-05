package org.dspace.xoai.data;

import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.data.AbstractResumptionTokenFormat;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;
import com.lyncode.xoai.util.DateUtils;


public class DSpaceResumptionTokenFormat extends AbstractResumptionTokenFormat {
    private static Logger log = LogManager
            .getLogger(DSpaceResumptionTokenFormat.class);

    public DSpaceResumptionTokenFormat() {

        // TODO Auto-generated constructor stub
    }





    @Override
    public ResumptionToken parse(String resumptionToken) throws BadResumptionToken {
        String[] res = resumptionToken.split("/", -1);
        if (res.length != 5) throw new BadResumptionToken();
        else {
            try {
                int offset = Integer.parseInt(res[4]);
                String prefix = (res[0].equals("")) ? null : res[0];
                String set = (res[3].equals("")) ? null : res[3];
                Date from = (res[1].equals("")) ? null : DateUtils.parse(res[1]);
                Date until = res[2].equals("") ? null : DateUtils.parse(res[2]);
                return new ResumptionToken(offset, prefix, set, from, until);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BadResumptionToken();
            }
        }
    }


    @Override
    public String format(ResumptionToken resumptionToken) {
        String result = "";
        if (resumptionToken.hasMetadataPrefix())
            result += resumptionToken.getMetadatePrefix();
        result += "/";
        if (resumptionToken.hasFrom())
            result += DateUtils.format(resumptionToken.getFrom());
        result += "/";
        if (resumptionToken.hasUntil())
            result += DateUtils.format(resumptionToken.getUntil());
        result += "/";
        if (resumptionToken.hasSet())
            result += resumptionToken.getSet();
        result += "/";
        result += resumptionToken.getOffset();
        return result;
    }

}
