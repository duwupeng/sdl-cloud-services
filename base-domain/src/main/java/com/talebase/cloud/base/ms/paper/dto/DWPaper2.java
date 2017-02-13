package com.talebase.cloud.base.ms.paper.dto;

import java.util.ArrayList;
import java.util.List;

/**题库新建与修改中的返回
 * Created by eric.du on 2016-12-6.
 */
public class DWPaper2 extends DItem{
    /**
     * 试卷名称
     */
    String name;
    /**
     * 0 , 新建 ; 1,修改
     */
    int status;

    List<DWPage> pages;

    int pageNumber = 0;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DWPage> getPages() {
        return pages;
    }

    public void addPage(DWPage page) {
        if(pages==null){
            pages = new ArrayList<DWPage>();
        }
        this.pages .add(page) ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPages(List<DWPage> pages) {
        this.pages = pages;
    }

    public int getPageNumber() {
        this.pageNumber = pages.size();
        return this.pageNumber;
    }
}
