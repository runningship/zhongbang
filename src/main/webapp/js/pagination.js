/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2014-06-17 10:55:28
 * @version $Id$
 */

/*
p   当前页码   
pn  总页码
ln  总条数

autoCount      
currentPageNo  
firstOfPage    
lastOfPage     
pageSize       
pageUrl        
totalCount     
totalPageCount
*/
var Page={
  'p':'',
  'pn':'',
  'ln':'',
  'prev_disabled':'',
  'next_disabled':'',
  'pageInfos':'',
  'pageNumInfos':'',
  'minp':'',
  'maxp':'',
  'nump':'',
  'pageHtml':'',
  'btn_css':' btn-primary',
  setPageInfo:function(page){
      this.p=page['currentPageNo'];
      this.pn=page['totalPageCount'];
      this.ln=page['totalCount'];
      $('.foot_page_box').html("");
      this.pageHtml=""
      if(this.p<=1){
        this.prev_disabled=" disabled='disabled'"
        this.next_disabled=""
      }else if(this.p>=this.pn){
        this.next_disabled=" disabled='disabled'"
        this.prev_disabled=""
      }else{
        this.next_disabled=""
        this.prev_disabled=""
      }

    if(this.pn <= 10){
      for(var i = 1;i<=this.pn;i++){  
        if(this.p == i){  
            this.pageHtml+='<button type="button" class="btn btn-default btn_p_list '+this.btn_css+'">'+i+'</button>';
        }else{  
            this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+i+'</button>';
        }  
      }
    }else{
      if(this.p<4){  
        for(var i = this.p-1;i>0;i--){  
            this.pageHtml='<button type="button" class="btn btn-default btn_p_list">'+i+'</button>'+this.pageHtml; 
        }  
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list '+this.btn_css+'">'+this.p+'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+ (this.p+1) +'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+ (this.p+2) +'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default">...</button>';
      }
      if(this.p>=4 && (this.p<=this.pn-3)){
        this.pageHtml+='<button type="button" class="btn btn-default">...</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+ (this.p-2) +'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+ (this.p-1) +'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list '+this.btn_css+'">'+this.p+'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+ (this.p+1) +'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+ (this.p+2) +'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default">...</button>';
      }
      if(this.p>this.pn-3){
        this.pageHtml+='<button type="button" class="btn btn-default">...</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+ (this.p-2) +'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+ (this.p-1) +'</button>';
        this.pageHtml+='<button type="button" class="btn btn-default btn_p_list '+this.btn_css+'">'+this.p+'</button>';
        for(var i = this.p+1;i<=this.pn;i++){
            this.pageHtml+='<button type="button" class="btn btn-default btn_p_list">'+i+'</button>';
        }
      }
    }


      var pageInfoStrs=''+
      '<div class="btn-group page_btn_group">'+
      '  <button type="button" class="btn btn-default">'+this.ln+'</button>'+
      '  <button type="button" class="btn btn-default">'+this.p+'/'+this.pn+'</button>'+
      '</div>'+
      '<div class="btn-group">';
      pageInfoStrs=pageInfoStrs+
      '  <button type="button" class="btn btn-default btn_p_prev_no"'+this.prev_disabled+'>首页</button>';
      pageInfoStrs=pageInfoStrs+
      '  <button type="button" class="btn btn-default btn_p_prev"'+this.prev_disabled+'>上一页</button>';
      pageInfoStrs=pageInfoStrs+this.pageHtml;
      pageInfoStrs=pageInfoStrs+
      '  <button type="button" class="btn btn-default btn_p_next"'+this.next_disabled+'>下一页</button>';
      pageInfoStrs=pageInfoStrs+
      '  <button type="button" class="btn btn-default btn_p_next_no"'+this.next_disabled+'>尾页</button>'+
      '</div>';

      $('.foot_page_box').html(pageInfoStrs);
    },
    Init:function(){
      $('form[name=form1]').append('<input type="hidden" class="pageInput" name="currentPageNo" value="">');
      $(document).on('click', '.btn_p_prev_no', function(event) {
        $('.pageInput').val(1);
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      }).on('click', '.btn_p_prev', function(event) {
        if(Page.p-1<=Page.pn){$('.pageInput').val(Page.p-1);}
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      }).on('click', '.btn_p_next', function(event) {
        if(Page.p+1<=Page.pn){$('.pageInput').val(Page.p+1);}
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      }).on('click', '.btn_p_next_no', function(event) {
        $('.pageInput').val(Page.pn);
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      }).on('click', '.btn_p_list', function(event) {
        var ThiVal=$(this).text();
      //  alert($(this).text())
        $('.pageInput').val(ThiVal);
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      });
    }
}