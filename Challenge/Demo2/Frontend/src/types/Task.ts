export interface Reminder {
  id: string;
  time: string;
  message: string;
}

export interface Comment {
  id: string;
  content: string;
  createdAt: string;
  createdBy: string;
}

export interface Task {
  id: string;
  title: string;
  description?: string;
  status: 'todo' | 'inprogress' | 'done';
  deadline?: string;
  assignee: string;
  createdBy: string;
  project?: string;
  labels?: string[];
  priority?: 'low' | 'medium' | 'high';
  reminders?: Reminder[];
  comments?: Comment[];
  deletedAt?: string;
}
