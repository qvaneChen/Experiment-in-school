#pragma once


// CCHENWENQIANLDlgLine �Ի���

class CCHENWENQIANLDlgLine : public CDialogEx
{
	DECLARE_DYNAMIC(CCHENWENQIANLDlgLine)

public:
	CCHENWENQIANLDlgLine(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CCHENWENQIANLDlgLine();

// �Ի�������
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_DLG_LINEDDA };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()
};
