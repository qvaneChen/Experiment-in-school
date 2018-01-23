#include "resource.h"
#include <Windows.h>
#include <math.h>

#define PI 3.14159

HINSTANCE hinst = NULL;
HWND hwndDlg = NULL;
POINT tranPt[3];//二维图形变换中三角形三个顶点的坐标
int m_wndx0 = 710, m_wndy0 = 310;//坐标系原点坐标

/*生成DDA算法展示对话框*/
extern "C" __declspec(dllexport) void ShowDlgLineDDA();
/*生成Bresenham算法展示对话框*/
extern "C" __declspec(dllexport) void ShowDlgChange();
/*生成二维图形变换展示对话框*/
extern "C" __declspec(dllexport) void ShowDlgLineBRE();

/*DDA算法直线生成展示窗口消息处理*/
BOOL CALLBACK DlgLineDDA(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam);
/*Bresenham算法直线生成展示窗口消息处理*/
BOOL CALLBACK DlgLineBRE(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam);
/*二维图形变换展示窗口消息处理*/
BOOL CALLBACK DlgChange(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam);


BOOL APIENTRY DllMain(HANDLE hModule,
	DWORD ul_reason_for_call,
	LPVOID lpReserved
)
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
		hinst = (HINSTANCE)hModule;
	case DLL_PROCESS_DETACH:
		break;
	}
	return TRUE;
}

/*生成DDA算法展示对话框*/
extern "C" __declspec(dllexport) void ShowDlgLineDDA()
{
	hwndDlg = CreateDialog(hinst, MAKEINTRESOURCE(IDD_DLG_LINEDDA), NULL, (DLGPROC)DlgLineDDA);
	ShowWindow(hwndDlg, SW_SHOW);
}

/*生成Bresenham算法展示对话框*/
extern "C" __declspec(dllexport) void ShowDlgLineBRE()
{
	hwndDlg = CreateDialog(hinst, MAKEINTRESOURCE(IDD_DLG_LINEBRE), NULL, (DLGPROC)DlgLineBRE);
	ShowWindow(hwndDlg, SW_SHOW);
}

/*生成二维图形变换展示对话框*/
extern "C" __declspec(dllexport) void ShowDlgChange()
{
	hwndDlg = CreateDialog(hinst, MAKEINTRESOURCE(IDD_DLG_CHANGE), NULL, (DLGPROC)DlgChange);
	ShowWindow(hwndDlg, SW_SHOW);
}

/*用DDA算法绘制直线*/
static void DrawLineDDA(HDC hDC, int x0, int y0, int x1, int y1, COLORREF color)
{
	HPEN hPen = CreatePen(PS_SOLID, 1, color);
	HPEN hOldPen = (HPEN)SelectObject(hDC, hPen);
	SelectObject(hDC, hOldPen);

	int dx = x1 - x0, dy = y1 - y0;
	int temp, z;
	float x = x0, y = y0;
	float xTemp, yTemp;

	if (fabs(dx) > fabs(dy))
		temp = fabs(dx);
	else
		temp = fabs(dy);

	xTemp = float(dx) / float(temp);
	yTemp = float(dy) / float(temp);
	SetPixel(hDC, round(x), round(y), color);

	for (z = 0; z < temp; z++)
	{
		x += xTemp;
		y += yTemp;
		SetPixel(hDC, round(x), round(y), color);
	}

	DeleteObject(hPen);
}

/*用Bresenham算法绘制直线*/
static void DrawLineBRE(HDC hDC, int x0, int y0, int x1, int y1, COLORREF color)
{
	HPEN hPen = CreatePen(PS_SOLID, 1, color);
	HPEN hOldPen = (HPEN)SelectObject(hDC, hPen);
	SelectObject(hDC, hOldPen);

	int x, y, dx, dy, temp;

	if (fabs(x0 - x1) > fabs(y0 - y1))
	{
		dx = x1 - x0;
		dy = y1 - y0;
		temp = -dx;
		x = x0;
		y = y0;
		for (int i = 0; i <= dx; i++)
		{
			SetPixel(hDC, x, y, color);
			x = x + 1;
			temp = temp + 2 * dy;
			if (temp >= 0)
			{
				y = y + 1;
				temp = temp - 2 * dx;
			}
		}
	}
	else if (fabs(x0 - x1) > fabs(y0 - y1))
	{
		dx = x1 - x0;
		dy = y1 - y0;
		temp = -dx;
		x = x0;
		y = y0;
		for (int i = 0; i <= dx; i++)
		{
			SetPixel(hDC, x, y, color);
			y = y + 1;
			temp = temp + 2 * dx;
			if (temp >= 0)
			{
				x = x + 1;
				temp = temp - 2 * dy;
			}
		}
	}

	DeleteObject(hPen);
}

/*对二维图形变换的窗口进行绘制*/
static void DrawScene(HDC hDC, int style, int width, COLORREF color)
{
	//绘制背景坐标系
	Rectangle(hDC, 220, 20, 1200, 600);
	//画坐标轴
	MoveToEx(hDC, 220, 310, NULL);
	LineTo(hDC, 1200, 310);
	MoveToEx(hDC, 710, 20, NULL);
	LineTo(hDC, 710, 600);

	//设置画笔
	HPEN hPen = CreatePen(style, width, color);
	HPEN hOldPen = (HPEN)SelectObject(hDC, hPen);
	//绘制三角形
	MoveToEx(hDC, tranPt[0].x + m_wndx0, tranPt[0].y + m_wndy0, NULL);
	LineTo(hDC, tranPt[1].x + m_wndx0, tranPt[1].y + m_wndy0);
	LineTo(hDC, tranPt[2].x + m_wndx0, tranPt[2].y + m_wndy0);
	LineTo(hDC, tranPt[0].x + m_wndx0, tranPt[0].y + m_wndy0);

	DeleteObject(hPen);
}

/*平移变换*/
void TranslateTransform(int num)  
{
	int xTemp = 10;
	int yTemp = 10;
	switch (num) {
	case 0://up
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].y -= yTemp;
		}
		break;
	case 1://left
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].x -= xTemp;
		}
		break;
	case 2://down
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].y += yTemp;
		}
		break;
	case 3://right
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].x += xTemp;
		}
		break;
	}

}

/*旋转变换*/
void RotateTransform(int num)
{
	float roAngle = 30.0 * PI / 180.0;
	float temp;
	switch (num)
	{
	case 0://left
		for (int i = 0; i < 3; i++)
		{
			temp = tranPt[i].x;
			tranPt[i].x = tranPt[i].x * cos(roAngle) - tranPt[i].y * sin(roAngle);
			tranPt[i].y = temp * sin(roAngle) + tranPt[i].y * cos(roAngle);
		}
		break;
	case 1://right
		for (int i = 0; i < 3; i++)
		{
			temp = tranPt[i].y;
			tranPt[i].y = tranPt[i].y * cos(roAngle) + tranPt[i].x * sin(roAngle);
			tranPt[i].x = temp * sin(roAngle) - tranPt[i].x * cos(roAngle);
		}
		break;
	}

}

/*比例变换*/
void ScaleTransform(int num) 
{
	float ScaleB = 2.0;
	float ScaleS = 0.5;
	switch (num)
	{
	case 0://放大
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].x *= ScaleB;
			tranPt[i].y *= ScaleB;
		}
		break;
	case 1://缩小
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].x *= ScaleS;
			tranPt[i].y *= ScaleS;
		}
		break;
	}


}

/*对称变换*/
void SymmetryTransform(int num)
{
	switch (num)
	{
	case 0://-x
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].x *= -1;
		}
		break;
	case 1://-y
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].y *= -1;
		}
		break;
	case 2://-x&-y
		for (int i = 0; i < 3; i++)
		{
			tranPt[i].x *= -1;
			tranPt[i].y *= -1;
		}
		break;
	}

}

/*DDA算法直线生成展示窗口消息处理*/
BOOL CALLBACK DlgLineDDA(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	HDC         hDC;
	PAINTSTRUCT ps;
	switch (message)
	{
	case WM_INITDIALOG:
		return TRUE;
	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK)
			DialogBox(hinst, MAKEINTRESOURCE(IDD_DLG_CHANGE), hDlg, (DLGPROC)DlgChange);
		return TRUE;
	case WM_PAINT:
	{
		//调用函数利用GDI绘制窗口，展示DDA直线生成算法
		hDC = BeginPaint(hDlg, &ps);
		DrawLineDDA(hDC, 100, 40, 320, 60, RGB(0, 0, 0));
		DrawLineDDA(hDC, 60, 90, 350, 120, RGB(100, 0, 200));
		DrawLineDDA(hDC, 10, 150, 270, 170, RGB(100, 250, 100));
		DrawLineDDA(hDC, 30, 230, 380, 180, RGB(0, 250, 250));
		EndPaint(hDlg, &ps);
		return TRUE;
	}
	case WM_CLOSE:
		DestroyWindow(hDlg);
		hwndDlg = NULL;
		return TRUE;
	}
	return FALSE;
}

/*Bresenham算法直线生成展示窗口消息处理*/
BOOL CALLBACK DlgLineBRE(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	HDC         hDC;
	PAINTSTRUCT ps;
	switch (message)
	{
	case WM_INITDIALOG:
		return TRUE;
	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK)
			DialogBox(hinst, MAKEINTRESOURCE(IDD_DLG_CHANGE), hDlg, (DLGPROC)DlgChange);
		return TRUE;
	case WM_PAINT:
	{
		//调用函数利用GDI绘制窗口，展示Bresenham直线生成算法
		hDC = BeginPaint(hDlg, &ps);
		DrawLineBRE(hDC, 100, 30, 340, 40, RGB(0, 0, 0));
		DrawLineBRE(hDC, 60, 75, 300, 150, RGB(100, 0, 200));
		DrawLineBRE(hDC, 110, 50, 350, 60, RGB(250, 0, 250));
		DrawLineBRE(hDC, 100, 90, 320, 190, RGB(0, 250, 250));
		DrawLineBRE(hDC, 90, 60, 360, 70, RGB(100, 250, 100));
		EndPaint(hDlg, &ps);
		return TRUE;
	}
	case WM_CLOSE:
		DestroyWindow(hDlg);
		hwndDlg = NULL;
		return TRUE;
	}
	return FALSE;
}

/*二维图形变换展示窗口消息处理*/
BOOL CALLBACK DlgChange(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	HDC         hDC;
	PAINTSTRUCT ps;
	RECT rect;
	LPPOINT lppoint = { 0 };
	switch (message)
	{
	case WM_INITDIALOG:
		return TRUE;
	case WM_PAINT:
	{
		//调用函数利用GDI绘制二维图形变换展示窗口
		hDC = BeginPaint(hDlg, &ps);
		DrawScene(hDC, PS_SOLID, 2, RGB(250, 0, 250));
		EndPaint(hDlg, &ps);
		return TRUE;
	}
	case WM_COMMAND:
		switch (LOWORD(wParam))
		{
		case IDCBTNSCTX:
			tranPt[0].x = 100;
			tranPt[0].y = 200;
			tranPt[1].x = 200;
			tranPt[1].y = 200;
			tranPt[2].x = 200;
			tranPt[2].y = 50;
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNPYUP:
			TranslateTransform(0);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNPYLEFT:
			TranslateTransform(1);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNPYRIGHT:
			TranslateTransform(3);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNPYDOWN:
			TranslateTransform(2);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNCmX:
			SymmetryTransform(0);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNCmY:
			SymmetryTransform(1);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNCmXY:
			SymmetryTransform(2);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDCBTNLR:
			RotateTransform(0);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNRR:
			RotateTransform(1);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
		case IDBTNScaleS:
			ScaleTransform(1);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			//DialogBox(hinst, MAKEINTRESOURCE(IDD_DLG_CHANGE), hDlg, (DLGPROC)DlgChange);//测试按键消息接受
			return TRUE;
		case IDBTNScaleB:
			ScaleTransform(0);
			GetClientRect(hDlg, &rect);
			InvalidateRect(hDlg, &rect, TRUE);
			UpdateWindow(hDlg);
			return TRUE;
			break;
		}
		return TRUE;
	case WM_CLOSE:
		EndDialog(hDlg, NULL);
		hwndDlg = NULL;
		return TRUE;
	}
	return FALSE;
}


