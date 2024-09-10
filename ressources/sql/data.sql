INSERT INTO users (email, username, password)
VALUES
  ('jean@example.com', 'Jean', 'Password123!'),
  ('tom@example.com', 'Tom', 'Motdepasse123!'),
  ('claire@example.com', 'Claire', 'Test123!');

-- Insertion de données de test pour la table "subject"
INSERT INTO subjects (name, description)
VALUES
  ('Nutrition', 'Étude de l alimentation et de ses effets sur la santé.'),
  ('Fitness', 'Pratiques visant à améliorer la forme physique.'),
  ('Mental Health', 'Approche de la santé mentale et du bien-être psychologique.'),
  ('Wearable Technology', 'Dispositifs portables pour le suivi de la santé et de la performance.'),
  ('Sports Science', 'Étude des principes scientifiques appliqués au sport.'),
  ('Healthcare Technology', 'Utilisation des technologies pour améliorer les soins de santé.'),
  ('Telemedicine', 'Pratique de la médecine à distance à l aide de la technologie.'),
  ('Personal Training', 'Accompagnement individuel pour atteindre des objectifs de fitness.'),
  ('Wellness', 'Approche holistique du bien-être physique et mental.');


-- Insertion de données de test pour la table "article"
INSERT INTO articles (subject_id, user_id, title, content, published_at)
VALUES
  (1, 1, 'Introduction to Nutrition', 'La nutrition est essentielle pour maintenir une bonne santé. Découvrez les bases d une alimentation équilibrée.', '2024-06-06 14:30:00'),
  (2, 2, 'Getting Started with Fitness', 'Le fitness est la clé d une vie saine. Apprenez comment démarrer un programme d entraînement efficace.', '2024-06-06 14:30:00'),
  (3, 1, 'Understanding Mental Health', 'La santé mentale est tout aussi importante que la santé physique. Apprenez à prendre soin de votre bien-être psychologique.', '2024-06-06 14:30:00'),
  (4, 3, 'Introduction to Wearable Technology', 'Les technologies portables révolutionnent le suivi de la santé. Découvrez comment elles peuvent améliorer votre quotidien.', '2024-06-06 14:30:00'),
  (1, 2, 'Advanced Nutrition Strategies', 'Explorez des stratégies nutritionnelles avancées pour optimiser votre santé et vos performances.', '2024-06-07 10:15:00'),
  (3, 2, 'Mindfulness and Mental Health', 'Découvrez comment la pleine conscience peut améliorer votre santé mentale et réduire le stress.', '2024-06-08 11:10:00'),
  (1, 3, 'Nutrition for Athletes', 'Comprendre l importance de la nutrition dans l amélioration des performances sportives.', '2024-06-11 12:20:00'),
  (4, 1, 'Wearables in Sports', 'Analysez comment les technologies portables impactent les performances sportives et le suivi des athlètes.', '2024-06-09 09:45:00'),
  (2, 3, 'Building a Fitness Routine', 'Apprenez à créer une routine de fitness personnalisée pour atteindre vos objectifs.', '2024-06-10 15:55:00'),
  (2, 1, 'High-Intensity Interval Training (HIIT)', 'Découvrez les bienfaits du HIIT pour améliorer la condition physique en peu de temps.', '2024-06-12 14:30:00');

-- Insertion de données de test pour la table "comment"
INSERT INTO comments (article_id, user_id, content, created_at)
VALUES
  (1, 2, 'This article offers great insights into nutrition. Really appreciated the tips!', '2024-06-06 14:30:00'),
  (1, 3, 'The information was very useful for improving my diet. Thanks for the detailed explanation.', '2024-06-06 14:30:00'),
  (3, 1, 'I found the discussion on mental health really enlightening. It’s crucial to address these issues.', '2024-06-06 14:30:00');

-- Insertion de données de test pour la table "subscription"
INSERT INTO subscriptions (user_id, subject_id)
VALUES
  (1, 1),
  (1, 2),
  (2, 3),
  (3, 4);