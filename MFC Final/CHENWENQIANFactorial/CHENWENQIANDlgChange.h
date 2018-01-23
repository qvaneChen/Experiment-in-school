#pragma once


// CCHENWENQIANDlgChange 对话框

class CCHENWENQIANDlgChange : public CDialogEx
{
	DECLARE_DYNAMIC(CCHENWENQIANDlgChange)

public:
	CCHENWENQIANDlgChange(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CCHENWENQIANDlgChange();

// 对话框数据
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_DLG_CHANGE };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
};
