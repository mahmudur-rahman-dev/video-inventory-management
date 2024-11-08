"use client"

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { VideoUpload } from "@/components/admin/VideoUpload"
import { VideoManagement } from "@/components/admin/VideoManagement"
import { UserAssignment } from "@/components/admin/UserAssignment"
import { ActivityLog } from "@/components/admin/ActivityLog"
import { Container } from "@/components/ui/container"
import { Heading } from "@/components/ui/heading"
import { Button } from "@/components/ui/button"
import { useAuth } from "@/providers/auth-provider"
import { UserPlus, FileVideo, ListVideo, ActivitySquare, LogOut } from "lucide-react"
import { useAdminStorage, clearAllAdminStorage } from "@/hooks/use-admin-storage"

type TabValue = "upload" | "manage" | "assign" | "activity"

export default function AdminDashboard() {
  const [activeTab, setActiveTab, clearActiveTab] = useAdminStorage<TabValue>("dashboard_tab", "upload")
  const { isAuthenticated, user, logout } = useAuth()
  const router = useRouter()

  useEffect(() => {
    if (!isAuthenticated || !user?.roles.includes('ROLE_ADMIN')) {
      router.push('/login')
    }
  }, [isAuthenticated, user, router])

  const handleLogout = async () => {
    try {
      // Clear all admin-related storage before logout
      clearAllAdminStorage()
      await logout()
      router.push('/login')
    } catch (error) {
      console.error('Logout error:', error)
    }
  }

  return (
    <Container>
      <div className="flex justify-between items-center mb-6">
        <Heading 
          title="Admin Dashboard" 
          description={`Welcome Back ${user?.username}`}
        />
        <Button onClick={handleLogout} variant="outline">
          <LogOut className="h-4 w-4 mr-2" />
          Logout
        </Button>
      </div>
      <Tabs value={activeTab} onValueChange={(value) => setActiveTab(value as TabValue)} className="space-y-4">
        <TabsList className="grid grid-cols-4 gap-4">
          <TabsTrigger value="upload" className="flex items-center gap-2">
            <FileVideo className="h-4 w-4" />
            Upload Video
          </TabsTrigger>
          <TabsTrigger value="manage" className="flex items-center gap-2">
            <ListVideo className="h-4 w-4" />
            Manage Videos
          </TabsTrigger>
          <TabsTrigger value="assign" className="flex items-center gap-2">
            <UserPlus className="h-4 w-4" />
            Assign Videos
          </TabsTrigger>
          <TabsTrigger value="activity" className="flex items-center gap-2">
            <ActivitySquare className="h-4 w-4" />
            Activity Log
          </TabsTrigger>
        </TabsList>
        <TabsContent value="upload" className="p-4 border rounded-lg">
          <VideoUpload />
        </TabsContent>
        <TabsContent value="manage" className="p-4 border rounded-lg">
          <VideoManagement />
        </TabsContent>
        <TabsContent value="assign" className="p-4 border rounded-lg">
          <UserAssignment />
        </TabsContent>
        <TabsContent value="activity" className="p-4 border rounded-lg">
          <ActivityLog />
        </TabsContent>
      </Tabs>
    </Container>
  )
}