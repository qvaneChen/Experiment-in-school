#pragma once


// CCHENWENQIANDlgChange �Ի���

class CCHENWENQIANDlgChange : public CDialogEx
{
	DECLARE_DYNAMIC(CCHENWENQIANDlgChange)

public:
	CCHENWENQIANDlgChange(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CCHENWENQIANDlgChange();

// �Ի�������
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_DLG_CHANGE };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()
};
