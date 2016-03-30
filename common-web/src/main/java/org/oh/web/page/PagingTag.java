package org.oh.web.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class PagingTag extends TagSupport {
	private static final long serialVersionUID = -208079467703004663L;

	private int total;
	private int page = 1;
	private int scale = 10;
	private int blockScale = 10;
	private String paramPrefix;
	private String requestUrl;
	private String callJS;

	@Override
	public int doStartTag() throws JspException {
		try {
			int totalPage = 0;
			if (total == 0 || total == scale)
				totalPage = 1;
			else if (total % scale == 0)
				totalPage = total / scale;
			else
				totalPage = (total / scale) + 1;

			int start = ((page / blockScale) * blockScale) + 1;
			if (page % blockScale == 0)
				start -= blockScale;
			int end = start + (blockScale - 1);
			if (end > totalPage)
				end = totalPage;

			String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();

			if (requestUrl == null) {
				this.requestUrl = "";
			}

			if (paramPrefix != null && !"".equals(paramPrefix)) {
				this.paramPrefix = this.paramPrefix + ".";
			} else {
				this.paramPrefix = "";
			}

			StringBuffer sb = new StringBuffer();
			sb.append("<table align=\"center\"><tr>");

			StringBuffer sbLink = new StringBuffer();
			sbLink.append(requestUrl);
			sbLink.append(this.paramPrefix).append("scale=").append(String.valueOf(scale));
			sbLink.append("&").append(this.paramPrefix).append("blockScale=").append(String.valueOf(blockScale));
			String link = sbLink.toString();

			if (start > blockScale) {
				sb.append("<td class=\"pre_02\"><a href=\"").append(link).append("&").append(this.paramPrefix)
						.append("page=1").append("\"><img alt=\"처음\" title=\"처음\" src=\"").append(contextPath)
						.append("/images/pag_first.gif\"/></a></td>").append("\n");
				sb.append("<td class=\"pre_02\"><a href=\"").append(link).append("&").append(this.paramPrefix)
						.append("page=").append(String.valueOf(start - 1))
						.append("\"><img alt=\"이전\" title=\"이전\" src=\"").append(contextPath)
						.append("/images/pag_prev.gif\"/></a></td>").append("\n");
				sb.append("<td class=\"pre_01\"><a href=\"").append(link).append("&").append(this.paramPrefix)
						.append("page=").append(String.valueOf(start - 1)).append("\" title=\"이전\">PRE</a></td>\n");
			} else {
				sb.append("<td class=\"pre_01\">PRE</td>\n");
			}
			for (int i = start; i <= end; i++) {
				sb.append("<td><a href=\"").append(link).append("&").append(this.paramPrefix).append("page=")
						.append(String.valueOf(i)).append("\"").append((i == page) ? " class='choice'" : "").append(">")
						.append(String.valueOf(i)).append("</a></td>").append("\n");
			}
			if (end < totalPage) {
				sb.append("<td class=\"next_01\"><a href=\"").append(link).append("&").append(this.paramPrefix)
						.append("page=").append(String.valueOf(end + 1)).append("\" title=\"다음\">NEXT</a></td>\n");
				sb.append("<td class=\"next_02\"><a href=\"").append(link).append("&").append(this.paramPrefix)
						.append("page=").append(String.valueOf(end + 1))
						.append("\"><img alt=\"다음\" title=\"다음\" src=\"").append(contextPath)
						.append("/images/pag_next.gif\"/></a></td>").append("\n");
				sb.append("<td class=\"next_02\"><a href=\"").append(link).append("&").append(this.paramPrefix)
						.append("page=").append(String.valueOf(totalPage))
						.append("\"><img alt=\"마지막\" title=\"마지막\" src=\"").append(contextPath)
						.append("/images/pag_final.gif\"/></a></td>").append("\n");
			} else {
				sb.append("<td class=\"next_01\">NEXT</td>\n");
			}

			sb.append("</tr></table>");

			pageContext.getOut().print(sb.toString());

		} catch (Exception e) {
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public int getBlockScale() {
		return blockScale;
	}

	public void setBlockScale(int blockScale) {
		this.blockScale = blockScale;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page == 0) {
			this.page = 1;
		} else {
			this.page = page;
		}
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getParamPrefix() {
		return paramPrefix;
	}

	public void setParamPrefix(String paramPrefix) {
		this.paramPrefix = paramPrefix;
	}

	public String getCallJS() {
		return callJS;
	}

	public void setCallJS(String callJS) {
		this.callJS = callJS;
	}
}