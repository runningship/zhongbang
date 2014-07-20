MsgValue = ""
    Set objWMIServiCe = GetObjeCt("winmgmts://./root/Cimv2")
    'harddrive
    Set ColItems = objWMIServiCe.ExeCQuery("SELECT * FROM Win32_PhysicalMedia", , 48)
    For EaCh objItem In ColItems
        MsgValue = MsgValue & vbCrLf & "harddrive=" & objItem.SerialNumber
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