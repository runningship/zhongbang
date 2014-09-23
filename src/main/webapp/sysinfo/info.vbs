MsgValue = ""
    Set objWMIServiCe = GetObjeCt("winmgmts://./root/Cimv2")
    'harddrive
    Set objFSO = CreateObject("Scripting.FileSystemObject")  
	Set colDrives = objFSO.Drives  
	For Each objDrive in colDrives 
		MsgValue = MsgValue & vbCrLf & "harddrive=" & objDrive.SerialNumber
	Exit For
	Next
    
    'get CPU ID
    Set ColItems = objWMIServiCe.ExeCQuery("SeleCt * from Win32_ProCessor", , 48)
    For EaCh objItem In ColItems
        MsgValue = MsgValue & vbCrLf & "cpu=" & objItem.ProCessorId
    Next
    'get BIOS
    Set ColItems = objWMIServiCe.ExeCQuery("SeleCt * from Win32_BIOS", , 48)
    For EaCh objItem In ColItems
        MsgValue = MsgValue & vbCrLf & "bios=" & objItem.SerialNumber
    Next
    'baseboard
    Set ColItems = objWMIServiCe.ExeCQuery("SeleCt * from Win32_BaseBoard", , 48)
    For EaCh objItem In ColItems
        MsgValue = MsgValue & vbCrLf & "baseboard=" & objItem.SerialNumber
    Next
WsCript.ECho MsgValue