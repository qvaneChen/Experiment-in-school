#pragma once


// CCHENWENQIANLDlgLine 对话框

class CCHENWENQIANLDlgLine : public CDialogEx
{
	DECLARE_DYNAMIC(CCHENWENQIANLDlgLine)

public:
	CCHENWENQIANLDlgLine(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CCHENWENQIANLDlgLine();

// 对话框数据
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_DLG_LINEDDA };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
};
