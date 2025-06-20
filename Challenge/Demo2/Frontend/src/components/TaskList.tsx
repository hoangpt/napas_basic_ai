import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Badge, Button, Form } from 'react-bootstrap';
import type { Task } from '../types/Task';
import { TaskService } from '../services/TaskService';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';

const TaskList = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [showAddForm, setShowAddForm] = useState(false);
  const [selectedTask, setSelectedTask] = useState<Task | null>(null);  type FormData = {
    title: string;
    description: string;
    status: Task['status'];
    deadline: Date;
    assignee: string;
    priority: Task['priority'];
    project: string;
    labels: string;
  };

  const [formData, setFormData] = useState<FormData>({
    title: '',
    description: '',
    status: 'todo',
    deadline: new Date(),
    assignee: '',
    priority: 'medium',
    project: '',
    labels: ''
  });

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const data = await TaskService.getAllTasks();
      setTasks(data);
    } catch (error) {
      console.error('Error fetching tasks:', error);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleDateChange = (date: Date | null) => {
    setFormData(prev => ({ ...prev, deadline: date || new Date() }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const taskData = {
        ...formData,
        labels: formData.labels.split(',').map(label => label.trim()),
        createdBy: 'currentUser', // Replace with actual user ID
        deadline: formData.deadline.toISOString()
      };

      if (selectedTask) {
        await TaskService.updateTask(selectedTask.id, taskData);
      } else {
        await TaskService.createTask(taskData as Omit<Task, 'id'>);
      }

      fetchTasks();
      resetForm();
    } catch (error) {
      console.error('Error saving task:', error);
    }
  };

  const resetForm = () => {
    setFormData({
      title: '',
      description: '',
      status: 'todo',
      deadline: new Date(),
      assignee: '',
      priority: 'medium',
      project: '',
      labels: ''
    });
    setSelectedTask(null);
    setShowAddForm(false);
  };

  const handleEdit = (task: Task) => {
    setSelectedTask(task);
    setFormData({
      title: task.title,
      description: task.description || '',
      status: task.status,
      deadline: task.deadline ? new Date(task.deadline) : new Date(),
      assignee: task.assignee,
      priority: task.priority || 'medium',
      project: task.project || '',
      labels: task.labels?.join(', ') || ''
    });
    setShowAddForm(true);
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await TaskService.deleteTask(id);
        fetchTasks();
      } catch (error) {
        console.error('Error deleting task:', error);
      }
    }
  };

  const getStatusBadgeVariant = (status: Task['status']) => {
    switch (status) {
      case 'todo': return 'secondary';
      case 'inprogress': return 'primary';
      case 'done': return 'success';
      default: return 'secondary';
    }
  };

  const getPriorityBadgeVariant = (priority?: Task['priority']) => {
    switch (priority) {
      case 'high': return 'danger';
      case 'medium': return 'warning';
      case 'low': return 'info';
      default: return 'secondary';
    }
  };

  return (
    <Container fluid className="py-4">
      <Row className="mb-4">
        <Col>
          <h1 className="h3">Task Management</h1>
          <Button 
            variant="primary" 
            onClick={() => setShowAddForm(!showAddForm)}
            className="mb-3"
          >
            {showAddForm ? 'Cancel' : 'Add New Task'}
          </Button>
        </Col>
      </Row>

      {showAddForm && (
        <Row className="mb-4">
          <Col md={8} lg={6}>
            <Card>
              <Card.Body>
                <h4>{selectedTask ? 'Edit Task' : 'Add New Task'}</h4>
                <Form onSubmit={handleSubmit}>
                  <Form.Group className="mb-3">
                    <Form.Label>Title</Form.Label>
                    <Form.Control
                      type="text"
                      name="title"
                      value={formData.title}
                      onChange={handleInputChange}
                      required
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Description</Form.Label>
                    <Form.Control
                      as="textarea"
                      name="description"
                      value={formData.description}
                      onChange={handleInputChange}
                      rows={3}
                    />
                  </Form.Group>

                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label>Status</Form.Label>
                        <Form.Select
                          name="status"
                          value={formData.status}
                          onChange={handleInputChange}
                        >
                          <option value="todo">To Do</option>
                          <option value="inprogress">In Progress</option>
                          <option value="done">Done</option>
                        </Form.Select>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label>Priority</Form.Label>
                        <Form.Select
                          name="priority"
                          value={formData.priority}
                          onChange={handleInputChange}
                        >
                          <option value="low">Low</option>
                          <option value="medium">Medium</option>
                          <option value="high">High</option>
                        </Form.Select>
                      </Form.Group>
                    </Col>
                  </Row>

                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label>Deadline</Form.Label>
                        <DatePicker
                          selected={formData.deadline}
                          onChange={handleDateChange}
                          className="form-control"
                          dateFormat="yyyy-MM-dd"
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label>Assignee</Form.Label>
                        <Form.Control
                          type="text"
                          name="assignee"
                          value={formData.assignee}
                          onChange={handleInputChange}
                          required
                        />
                      </Form.Group>
                    </Col>
                  </Row>

                  <Form.Group className="mb-3">
                    <Form.Label>Project</Form.Label>
                    <Form.Control
                      type="text"
                      name="project"
                      value={formData.project}
                      onChange={handleInputChange}
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Labels (comma-separated)</Form.Label>
                    <Form.Control
                      type="text"
                      name="labels"
                      value={formData.labels}
                      onChange={handleInputChange}
                      placeholder="e.g., frontend, bug, feature"
                    />
                  </Form.Group>

                  <Button type="submit" variant="primary">
                    {selectedTask ? 'Update Task' : 'Create Task'}
                  </Button>
                  <Button variant="secondary" className="ms-2" onClick={resetForm}>
                    Cancel
                  </Button>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      )}

      <Row>
        {tasks.map(task => (
          <Col key={task.id} xs={12} md={6} lg={4} className="mb-4">
            <Card>
              <Card.Body>
                <div className="d-flex justify-content-between align-items-start mb-2">
                  <Card.Title>{task.title}</Card.Title>
                  <Badge bg={getStatusBadgeVariant(task.status)}>
                    {task.status}
                  </Badge>
                </div>
                
                <Card.Text>{task.description}</Card.Text>
                
                <div className="mb-2">
                  {task.priority && (
                    <Badge bg={getPriorityBadgeVariant(task.priority)} className="me-2">
                      {task.priority}
                    </Badge>
                  )}
                  {task.project && (
                    <Badge bg="info" className="me-2">
                      {task.project}
                    </Badge>
                  )}
                </div>

                {task.labels && task.labels.length > 0 && (
                  <div className="mb-2">
                    {task.labels.map((label, index) => (
                      <Badge key={index} bg="secondary" className="me-1">
                        {label}
                      </Badge>
                    ))}
                  </div>
                )}

                <div className="small text-muted mb-2">
                  <div>Assignee: {task.assignee}</div>
                  {task.deadline && (
                    <div>Deadline: {new Date(task.deadline).toLocaleDateString()}</div>
                  )}
                </div>

                <div className="d-flex justify-content-end gap-2">
                  <Button
                    variant="outline-primary"
                    size="sm"
                    onClick={() => handleEdit(task)}
                  >
                    Edit
                  </Button>
                  <Button
                    variant="outline-danger"
                    size="sm"
                    onClick={() => handleDelete(task.id)}
                  >
                    Delete
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default TaskList;
